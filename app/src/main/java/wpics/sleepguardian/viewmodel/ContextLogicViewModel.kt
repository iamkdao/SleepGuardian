package wpics.sleepguardian.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import wpics.sleepguardian.data.prefs.SensorCacheDataStore
import wpics.sleepguardian.domain.SleepDetectionUseCase
import wpics.sleepguardian.domain.models.ContextSnapshot
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ContextLogicUiState(
    val lux: Double?,
    val proximityNear: Boolean?,
    val charging: Boolean?,
    val lastUpdated: Long,
    val isDark: Boolean,
    val isNear: Boolean,
    val isCharging: Boolean,
    val ruleResult: Boolean
)

class ContextLogicViewModel(app: Application) : AndroidViewModel(app) {

    private val cache = SensorCacheDataStore(app.applicationContext)
    private val useCase = SleepDetectionUseCase(darkLuxThreshold = 10.0)

    val ui: StateFlow<ContextLogicUiState> =
        cache.cacheFlow
            .combine(cache.cacheFlow) { c1, _ ->
                val snapshot = ContextSnapshot(
                    lux = c1.lastLux,
                    proximityNear = c1.lastProximityNear,
                    charging = c1.lastCharging
                )
                val eval = useCase.evaluate(snapshot)

                ContextLogicUiState(
                    lux = c1.lastLux,
                    proximityNear = c1.lastProximityNear,
                    charging = c1.lastCharging,
                    lastUpdated = c1.lastUpdated,
                    isDark = eval.isDark,
                    isNear = eval.isNear,
                    isCharging = eval.isCharging,
                    ruleResult = eval.ruleResult
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ContextLogicUiState(
                    lux = null,
                    proximityNear = null,
                    charging = null,
                    lastUpdated = 0L,
                    isDark = false,
                    isNear = false,
                    isCharging = false,
                    ruleResult = false
                )
            )
}