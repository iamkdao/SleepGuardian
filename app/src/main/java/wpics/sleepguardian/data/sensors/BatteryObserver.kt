package wpics.sleepguardian.data.sensors

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager

data class BatteryState(
    val percent: Int?,      // null => unknown
    val charging: Boolean?  // null => unknown
)

class BatteryObserver(
    private val context: Context,
    private val onUpdate: (BatteryState) -> Unit
) {
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(ctx: Context?, intent: Intent?) {
            if (intent == null) return

            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)

            val pct =
                if (level >= 0 && scale > 0) ((level * 100f) / scale).toInt()
                else null

            val isCharging =
                when (status) {
                    BatteryManager.BATTERY_STATUS_CHARGING,
                    BatteryManager.BATTERY_STATUS_FULL -> true
                    BatteryManager.BATTERY_STATUS_DISCHARGING,
                    BatteryManager.BATTERY_STATUS_NOT_CHARGING -> false
                    else -> null
                }

            onUpdate(BatteryState(pct, isCharging))
        }
    }

    fun start() {
        // ACTION_BATTERY_CHANGED is sticky: you get an immediate callback
        context.registerReceiver(receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
    }

    fun stop() {
        runCatching { context.unregisterReceiver(receiver) }
    }
}