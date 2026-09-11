package by.alexandr7035.gitstat.data

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.core.DataSyncStatus
import by.alexandr7035.gitstat.core.ErrorType
import by.alexandr7035.gitstat.core.extensions.observeNullSafe
import by.alexandr7035.gitstat.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@AndroidEntryPoint
class SyncForegroundService: LifecycleService() {
    @Inject
    lateinit var syncRepository: DataSyncRepository
    private var job: Job? = null

    // ServiceCompat ignores the type argument on pre-Q platforms.
    @Suppress("InlinedApi")
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        // Init livedata for statuses update
        val statusLiveData = MutableLiveData<DataSyncStatus>()

        val notificationId = System.currentTimeMillis().toInt()

        ServiceCompat.startForeground(
            this,
            notificationId,
            getNotification(
                getString(R.string.sync_notification_title),
                getString(R.string.stage_profile)
            ),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
        )

        job = CoroutineScope(Dispatchers.IO).launch {
            syncRepository.syncData(statusLiveData)
            // A delay to prevent showing a success message in a separate notification
            delay(3.seconds)
            stopSelf()
        }

        statusLiveData.observeNullSafe(this) { syncStatus ->
            Timber.tag(TAG).d("Service: sync status changed $syncStatus")

            val notificationText = when (syncStatus) {
                is DataSyncStatus.PendingContributions -> getString(R.string.stage_contributions)
                is DataSyncStatus.PendingProfile -> getString(R.string.stage_profile)
                is DataSyncStatus.PendingRepos -> getString(R.string.stage_repositories)
                is DataSyncStatus.Failure -> {
                    when (syncStatus.error) {
                        ErrorType.FAILED_AUTHORIZATION -> {
                            getString(R.string.error_sync_authorization)
                        }
                        else -> {
                            getString(R.string.error_cant_get_data_remote)
                        }
                    }
                }
                is DataSyncStatus.Success -> getString(R.string.sync_success)
            }

            // Update notification on status changed
            val notification = getNotification(
                getString(R.string.sync_notification_title),
                notificationText
            )

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                NotificationManagerCompat.from(this).notify(notificationId, notification)
            }
        }

        // Do not restart if terminated
        return START_NOT_STICKY
    }

    // Called on Android 15+ when a dataSync foreground service hits its runtime limit.
    // The process is force-crashed if the service is still running shortly after, so stop cleanly.
    override fun onTimeout(startId: Int, fgsType: Int) {
        Timber.tag(TAG).d("service onTimeout()")
        job?.cancel()
        job = null
        stopSelf()
    }

    private fun getNotification(title: String, message: String): Notification {

        val notificationIntent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, getString(R.string.NOTIFICATION_CHANNEL_ID))
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_app_rounded)
            .setContentIntent(pendingIntent)
                // Make not dismissible
            .setOngoing(true)
                // Show notification immediately (prevent 10 sec delay)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .build()
    }


    override fun onDestroy() {
        Timber.tag(TAG).d("service onDestroy()")
        job?.cancel()
        job = null
        super.onDestroy()
    }

    private companion object {
        val TAG = SyncForegroundService::class.simpleName.toString()
    }
}