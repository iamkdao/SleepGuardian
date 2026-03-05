package wpics.sleepguardian.worker

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


private val Context.workerState by preferencesDataStore("worker_state")

class WorkerStateDataStore(private val context: Context) {

    private object Keys {
        val SLEEP_ACTIVE = booleanPreferencesKey("sleep_mode_active")
    }

    val sleepModeActiveFlow: Flow<Boolean> =
        context.workerState.data.map { prefs -> prefs[Keys.SLEEP_ACTIVE] ?: false }

    suspend fun setSleepModeActive(active: Boolean) {
        context.workerState.edit { prefs ->
            prefs[Keys.SLEEP_ACTIVE] = active
        }
    }
}