package sentinel.core.report

import sentinel.core.detector.Threat
import sentinel.core.type.RiskLevel

abstract class SecurityReport(
    open val threats: List<Threat>,
    open val threshold: Int,
    open val timestamp: Long,
) {
    open val isRooted: Boolean = false

    open val isJailbroken: Boolean = false

    open val isTampered: Boolean = false

    open val isHooked: Boolean = false

    open val isSimulator: Boolean = false

    open val isEmulator: Boolean = false

    open val isDebugged: Boolean = false

    open val isMockLocation: Boolean = false

    val severity: Int by lazy { threats.sumOf { threat -> threat.violation.severity } }

    val riskLevel: RiskLevel by lazy {
        RiskLevel.getLevel(severity = severity, threshold = threshold)
    }

    fun isSafe(): Boolean = riskLevel == RiskLevel.SAFE

    fun isCritical(): Boolean = riskLevel == RiskLevel.HIGH
}