//Gia Huy Phạm
//Ryan Zhang

package wpics.sleepguardian.domain.models

data class ContextSnapshot(
    val lux: Double?,             // null => unavailable
    val proximityNear: Boolean?,  // null => unavailable
    val charging: Boolean?        // null => unknown
)

data class RuleEvaluation(
    val isDark: Boolean,
    val isNear: Boolean,
    val isCharging: Boolean,
    val ruleResult: Boolean
)