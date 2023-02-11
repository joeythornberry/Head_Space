package com.example.head_space

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlin.random.Random.Default.nextInt

class OneTimeScheduleWorker(
    val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {

        Log.d("doing work", "notifying")

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)


        val CHANNEL_ID = "UNREAD_MESSAGES_CHANNEL_ID"
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification_icon)
            .setContentTitle("Head_Space")
            .setContentText("^open this app =)")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(context)) {
            Log.d("notifying","notifying")
            if(sendNotifications) {
                notify(nextInt(), builder.build())
            }
        }

        scheduleOneTimeNotification(notificationPeriod,context)

        return Result.success()
    }

}
