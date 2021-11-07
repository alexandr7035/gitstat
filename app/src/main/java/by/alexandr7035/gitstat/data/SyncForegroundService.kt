package by.alexandr7035.gitstat.data

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.IBinder
import androidx.core.app.NotificationCompat
import by.alexandr7035.gitstat.R
import by.alexandr7035.gitstat.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SyncForegroundService: Service() {

    @Inject lateinit var syncRepository: DataSyncRepository
    private var job: Job? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.tag("DEBUG_SERVICE").d("123")
        startForeground(System.currentTimeMillis().toInt(), getNotification())

        job = CoroutineScope(Dispatchers.IO).launch {
            syncRepository.test()
        }

        return START_NOT_STICKY
    }

    private fun getNotification(): Notification {

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        return NotificationCompat.Builder(this, getString(R.string.NOTIFICATION_CHANNEL_ID))
            .setContentTitle(getString(R.string.sync_notification_title))
            .setContentText(getString(R.string.sync_notification_text))
            .setSmallIcon(R.drawable.ic_app_rounded)
            .setContentIntent(pendingIntent)
            .build()
    }


    override fun onDestroy() {
        job?.cancel()
        job = null
        super.onDestroy()
    }

}