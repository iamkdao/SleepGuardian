//Gia Huy Phạm
//Ryan Zhang

package wpics.sleepguardian.data.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.sensorCache by preferencesDataStore(name = "sensor_cache")

data class SensorCache(
    val lastLux: Double?,                 // null => unavailable/unknown
    val lastProximityNear: Boolean?,       // null => unavailable/unknown
    val lastCharging: Boolean?,            // null => unavailable/unknown
    val lastUpdated: Long                 // ms since epoch
)

class SensorCacheDataStore(private val context: Context) {

    private object Keys {
        val LAST_LUX = doublePreferencesKey("lastLux")
        val HAS_LUX = booleanPreferencesKey("hasLux")

        val LAST_PROX_NEAR = booleanPreferencesKey("lastProximityNear")
        val HAS_PROX = booleanPreferencesKey("hasProximityNear")

        val LAST_CHARGING = booleanPreferencesKey("lastCharging")
        val HAS_CHARGING = booleanPreferencesKey("hasCharging")

        val LAST_UPDATED = longPreferencesKey("lastUpdated")
    }

    val cacheFlow: Flow<SensorCache> = context.sensorCache.data.map { prefs ->
        val hasLux = prefs[Keys.HAS_LUX] ?: false
        val hasProx = prefs[Keys.HAS_PROX] ?: false
        val hasCharging = prefs[Keys.HAS_CHARGING] ?: false

        SensorCache(
            lastLux = if (hasLux) prefs[Keys.LAST_LUX] else null,
            lastProximityNear = if (hasProx) prefs[Keys.LAST_PROX_NEAR] else null,
            lastCharging = if (hasCharging) prefs[Keys.LAST_CHARGING] else null,
            lastUpdated = prefs[Keys.LAST_UPDATED] ?: 0L
        )
    }

    suspend fun setLux(lux: Double?) {
        context.sensorCache.edit { prefs ->
            if (lux == null) {
                prefs[Keys.HAS_LUX] = false
                prefs.remove(Keys.LAST_LUX)
            } else {
                prefs[Keys.HAS_LUX] = true
                prefs[Keys.LAST_LUX] = lux
            }
            prefs[Keys.LAST_UPDATED] = System.currentTimeMillis()
        }
    }

    suspend fun setProximityNear(near: Boolean?) {
        context.sensorCache.edit { prefs ->
            if (near == null) {
                prefs[Keys.HAS_PROX] = false
                prefs.remove(Keys.LAST_PROX_NEAR)
            } else {
                prefs[Keys.HAS_PROX] = true
                prefs[Keys.LAST_PROX_NEAR] = near
            }
            prefs[Keys.LAST_UPDATED] = System.currentTimeMillis()
        }
    }

    suspend fun setCharging(charging: Boolean?) {
        context.sensorCache.edit { prefs ->
            if (charging == null) {
                prefs[Keys.HAS_CHARGING] = false
                prefs.remove(Keys.LAST_CHARGING)
            } else {
                prefs[Keys.HAS_CHARGING] = true
                prefs[Keys.LAST_CHARGING] = charging
            }
            prefs[Keys.LAST_UPDATED] = System.currentTimeMillis()
        }
    }
}