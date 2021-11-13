package by.alexandr7035.gitstat.data

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.core.DataSyncStatus
import by.alexandr7035.gitstat.extensions.debug
import by.alexandr7035.gitstat.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SyncForegroundService: LifecycleService() {

    @Inject lateinit var syncRepository: DataSyncRepository
    private var job: Job? = null
    private var statusLiveData: MutableLiveData<DataSyncStatus>? = null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        // Init livedata for statuses update
        statusLiveData = MutableLiveData()

        Timber.tag("DEBUG_SERVICE").d("123")

        val notificationId = System.currentTimeMillis().toInt()
        startForeground(notificationId, getNotification(
            getString(R.string.sync_notification_title),
            // Text for start sync stage here
            getString(R.string.stage_profile)
        ))

        job = CoroutineScope(Dispatchers.IO).launch {
            syncRepository.syncData(statusLiveData)
            // A delay to prevent showing a success message in a separate notification
            delay(3000)
            stopSelf()
        }

        statusLiveData?.observe(this, {
            Timber.debug("Service: sync status changed $it")

            val notificationText = when (it) {
                DataSyncStatus.PENDING_PROFILE -> getString(R.string.stage_profile)
                DataSyncStatus.PENDING_REPOSITORIES -> getString(R.string.stage_repositories)
                DataSyncStatus.PENDING_CONTRIBUTIONS -> getString(R.string.stage_contributions)
                DataSyncStatus.SUCCESS -> getString(R.string.sync_success)
                DataSyncStatus.FAILED_NETWORK -> getString(R.string.error_cant_get_data_remote)
                DataSyncStatus.AUTHORIZATION_ERROR -> getString(R.string.error_sync_authorization)
            }

            // Update notification on status changed
            val notification = getNotification(
                getString(R.string.sync_notification_title),
                notificationText
            )

            NotificationManagerCompat.from(this).notify(notificationId, notification)
        })

        return START_NOT_STICKY
    }

    private fun getNotification(title: String, message: String): Notification {

        val notificationIntent = Intent(this, MainActivity::class.java)

        val mutabilityFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE
        }
        else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, mutabilityFlag)

        return NotificationCompat.Builder(this, getString(R.string.NOTIFICATION_CHANNEL_ID))
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_app_rounded)
            .setContentIntent(pendingIntent)
            .build()
    }


    override fun onDestroy() {
        Timber.tag("DEBUG_SERVICE").d("service onDestroy()")

        job?.cancel()
        job = null
        statusLiveData = null
        super.onDestroy()
    }

}