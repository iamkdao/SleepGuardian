package wpics.sleepguardian.domain

import wpics.sleepguardian.domain.models.ContextSnapshot
import wpics.sleepguardian.domain.models.RuleEvaluation

class SleepDetectionUseCase(
    private val darkLuxThreshold: Double = 10.0
) {
    fun evaluate(snapshot: ContextSnapshot): RuleEvaluation {
        val isDark = snapshot.lux?.let { it < darkLuxThreshold } ?: false
        val isNear = snapshot.proximityNear == true
        val isCharging = snapshot.charging == true

        val ruleResult = isDark && isNear && isCharging
        return RuleEvaluation(
            isDark = isDark,
            isNear = isNear,
            isCharging = isCharging,
            ruleResult = ruleResult
        )
    }
}