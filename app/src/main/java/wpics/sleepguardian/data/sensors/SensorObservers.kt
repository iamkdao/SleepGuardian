package wpics.sleepguardian.data.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class LightSensorObserver(
    context: Context,
    private val onLux: (Float?) -> Unit // null => unavailable
) : SensorEventListener {

    private val sm = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor: Sensor? = sm.getDefaultSensor(Sensor.TYPE_LIGHT)

    fun start() {
        if (sensor == null) {
            onLux(null)
            return
        }
        sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stop() {
        sm.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        onLux(event.values.firstOrNull())
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}

class ProximitySensorObserver(
    context: Context,
    private val onNear: (Boolean?) -> Unit // null => unavailable
) : SensorEventListener {

    private val sm = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val sensor: Sensor? = sm.getDefaultSensor(Sensor.TYPE_PROXIMITY)

    fun start() {
        if (sensor == null) {
            onNear(null)
            return
        }
        sm.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stop() {
        sm.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        val v = event.values.firstOrNull()
        if (v == null || sensor == null) {
            onNear(null)
            return
        }
        val near = v < sensor.maximumRange
        onNear(near)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}