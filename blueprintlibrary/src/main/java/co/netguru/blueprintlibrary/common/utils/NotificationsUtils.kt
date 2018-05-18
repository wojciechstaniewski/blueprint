package co.netguru.blueprintlibrary.common.utils

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import co.netguru.blueprintlibrary.R
import co.netguru.blueprintlibrary.common.Constants
import javax.inject.Inject


class NotificationsUtils @Inject constructor(val context: Context) {

    fun <T : AppCompatActivity> showNotification(message: String?, activityClass: Class<T>) {
        val activityIntent = Intent(context, activityClass)
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification =
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createNotificationChannel(manager)
                    getNotificationBuilder(message, activityIntent).build()
                } else {
                    getNotificationCompatBuilder(message, activityIntent).build()
                }
        manager.notify(Constants.NOTIFICATION_CHANEL_ID, notification)
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun getNotificationBuilder(message: String?, activityIntent: Intent) =
            Notification.Builder(context, Constants.NOTIFICATION_CHANEL_NAME)
                    .setContentTitle(context.getString(R.string.new_message))
                    .setContentText(message)
                    .setSmallIcon(R.drawable.ic_netguru_logo_full)
                    .setAutoCancel(true)
                    .setContentIntent(
                            PendingIntent.getActivity(context, 0, activityIntent, 0))

    private fun getNotificationCompatBuilder(message: String?, activityIntent: Intent) =
            NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANEL_NAME)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(message ?: context.getString(R.string.new_message))
                    .setSmallIcon(R.drawable.ic_netguru_logo_full)
                    .setAutoCancel(true)
                    .setContentIntent(
                            PendingIntent.getActivity(context, 0, activityIntent, 0))

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(manager: NotificationManager) {
        val notificationChannel = NotificationChannel(Constants.NOTIFICATION_CHANEL_NAME,
                context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        manager.createNotificationChannel(notificationChannel)
    }

}