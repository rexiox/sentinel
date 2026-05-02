package sentinel.core.report

import sentinel.core.detector.Threat
import sentinel.core.violation.AndroidViolation

class AndroidSecurityReport(
    override val threats: List<Threat>,
    override val threshold: Int,
) : SecurityReport(
    threats = threats,
    threshold = threshold,
    timestamp = System.currentTimeMillis()
) {

    override val isRooted: Boolean
        get() {
            val score = threats
                .map(Threat::violation)
                .filterIsInstance<AndroidViolation.Root>()
                .sumOf(AndroidViolation.Root::severity)

            return score >= 90
        }

    override val isTampered: Boolean get() = threats.any { it.violation is AndroidViolation.Tamper }
    override val isHooked: Boolean get() = threats.any { it.violation is AndroidViolation.Hook }
    override val isEmulator: Boolean get() = threats.any { it.violation is AndroidViolation.Emulator }
    override val isDebugged: Boolean get() = threats.any { it.violation is AndroidViolation.Debugger }
    override val isMockLocation: Boolean get() = threats.any { it.violation is AndroidViolation.Location }
}