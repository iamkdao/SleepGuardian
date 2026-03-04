package wpics.sleepguardian.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import wpics.sleepguardian.data.prefs.SensorCacheDataStore
import wpics.sleepguardian.data.repo.SleepEventRepository
import wpics.sleepguardian.data.sensors.BatteryObserver
import wpics.sleepguardian.data.sensors.BatteryState
import wpics.sleepguardian.data.sensors.LightSensorObserver
import wpics.sleepguardian.data.sensors.ProximitySensorObserver
import wpics.sleepguardian.domain.SleepDetectionUseCase
import wpics.sleepguardian.domain.models.ContextSnapshot
import wpics.sleepguardian.data.db.SleepEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow as KStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DashboardSensorUiState(
    val lux: Float?,
    val proximityNear: Boolean?,
    val batteryPercent: Int?,
    val charging: Boolean?,
    val sleepModeActive: Boolean
)

class DashboardViewModel(app: Application) : AndroidViewModel(app) {

    private val prefs = SensorCacheDataStore(app.applicationContext)
    private val repo = SleepEventRepository(app.applicationContext)
    private val useCase = SleepDetectionUseCase(darkLuxThreshold = 10.0)

    // E2: observed by Dashboard UI
    val recentEvents: KStateFlow<List<SleepEvent>> =
        repo.observeRecent(limit = 25).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _ui = MutableStateFlow(
        DashboardSensorUiState(
            lux = null,
            proximityNear = null,
            batteryPercent = null,
            charging = null,
            sleepModeActive = false
        )
    )
    val ui: StateFlow<DashboardSensorUiState> = _ui

    private var lightObs: LightSensorObserver? = null
    private var proxObs: ProximitySensorObserver? = null
    private var batteryObs: BatteryObserver? = null

    private var lastLoggedSleepMode: Boolean? = null

    private fun recomputeSleepModeAndMaybeLog(reasonOn: String, reasonOff: String) {
        val snapshot = ContextSnapshot(
            lux = _ui.value.lux?.toDouble(),
            proximityNear = _ui.value.proximityNear,
            charging = _ui.value.charging
        )
        val eval = useCase.evaluate(snapshot)
        val newMode = eval.ruleResult

        _ui.value = _ui.value.copy(sleepModeActive = newMode)

        // Log only on transition (E1)
        val prev = lastLoggedSleepMode
        if (prev == null) {
            lastLoggedSleepMode = newMode
            return
        }
        if (prev != newMode) {
            lastLoggedSleepMode = newMode
            viewModelScope.launch {
                repo.insert(
                    SleepEvent(
                        timestampMs = System.currentTimeMillis(),
                        activated = newMode,
                        reason = if (newMode) reasonOn else reasonOff,
                        lux = snapshot.lux,
                        proximityNear = snapshot.proximityNear,
                        charging = snapshot.charging
                    )
                )
            }
        }
    }

    fun startObservers() {
        val ctx = getApplication<Application>().applicationContext
        if (lightObs != null || proxObs != null || batteryObs != null) return

        // Initialize baseline for transition detection
        lastLoggedSleepMode = _ui.value.sleepModeActive

        lightObs = LightSensorObserver(ctx) { lux ->
            _ui.value = _ui.value.copy(lux = lux)
            recomputeSleepModeAndMaybeLog(
                reasonOn = "Rule satisfied (dark + near + charging)",
                reasonOff = "Rule not satisfied"
            )
            viewModelScope.launch { prefs.setLux(lux?.toDouble()) }
        }.also { it.start() }

        proxObs = ProximitySensorObserver(ctx) { near ->
            _ui.value = _ui.value.copy(proximityNear = near)
            recomputeSleepModeAndMaybeLog(
                reasonOn = "Rule satisfied (dark + near + charging)",
                reasonOff = "Rule not satisfied"
            )
            viewModelScope.launch { prefs.setProximityNear(near) }
        }.also { it.start() }

        batteryObs = BatteryObserver(ctx) { bs: BatteryState ->
            _ui.value = _ui.value.copy(
                batteryPercent = bs.percent,
                charging = bs.charging
            )
            recomputeSleepModeAndMaybeLog(
                reasonOn = "Rule satisfied (dark + near + charging)",
                reasonOff = "Rule not satisfied"
            )
            viewModelScope.launch { prefs.setCharging(bs.charging) }
        }.also { it.start() }
    }

    fun stopObservers() {
        lightObs?.stop()
        proxObs?.stop()
        batteryObs?.stop()

        lightObs = null
        proxObs = null
        batteryObs = null
    }

    // Used by Settings to clear history (E3)
    fun clearHistory() {
        viewModelScope.launch { repo.clearAll() }
    }

    override fun onCleared() {
        stopObservers()
        super.onCleared()
    }
}