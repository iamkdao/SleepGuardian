//Gia Huy Phạm
//Ryan Zhang

package wpics.sleepguardian.worker.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import wpics.sleepguardian.R

object SleepNotification {
    private const val CHANNEL_ID = "sleepguardian_alerts"
    private const val NOTIF_ID = 1001

    private fun ensureChannel(ctx: Context) {
        val nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Sleep Guardian Alerts",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        nm.createNotificationChannel(channel)
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    fun showActivation(ctx: Context) {
        ensureChannel(ctx)

        val notif = NotificationCompat.Builder(ctx, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Sleep Mode Activated")
            .setContentText("Sleep Guardian detected bedtime conditions.")
            .setAutoCancel(true)
            .build()

        // If permission is missing on Android 13+, this will simply not show.
        NotificationManagerCompat.from(ctx).notify(NOTIF_ID, notif)
    }
}