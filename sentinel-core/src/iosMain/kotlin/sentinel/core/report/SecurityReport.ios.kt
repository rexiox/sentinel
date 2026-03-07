package sentinel.core.report

import sentinel.core.detector.Threat
import sentinel.core.violation.IosViolation
import kotlin.time.Clock

class IosSecurityReport(
    override val threats: List<Threat>,
    override val threshold: Int,
) : SecurityReport(
    threats = threats,
    threshold = threshold,
    timestamp = Clock.System.now().toEpochMilliseconds()
) {

    override val isJailbroken: Boolean get() = threats.any { it.violation is IosViolation.Jailbreak }
    override val isTampered: Boolean get() = threats.any { it.violation is IosViolation.Tamper }
    override val isHooked: Boolean get() = threats.any { it.violation is IosViolation.Hook }
    override val isSimulator: Boolean get() = threats.any { it.violation is IosViolation.Simulator }
    override val isDebugged: Boolean get() = threats.any { it.violation is IosViolation.Debugger }
    override val isMockLocation: Boolean get() = threats.any { it.violation is IosViolation.Location }
}