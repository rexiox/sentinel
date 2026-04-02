package sentinel.runtime

import sentinel.core.report.SecurityReport
import sentinel.core.violation.SecurityViolation
import sentinel.core.violation.IosViolation

actual class Scope : InstallScope() {

    actual fun dispatch(violation: SecurityViolation, report: SecurityReport) {
        when (violation) {
            is IosViolation.Jailbreak -> {
                if (report.isJailbroken) {
                    onCompromised?.invoke()
                }
            }

            is IosViolation.Hook -> onHooked?.invoke()
            is IosViolation.Simulator -> onSimulated?.invoke()
            is IosViolation.Debugger -> onDebugged?.invoke()
            is IosViolation.Tamper -> onTampered?.invoke()
        }

        when {
            report.isCritical() -> onCritical?.invoke(report.severity)
            report.isSafe() -> onSafe?.invoke()
        }
    }
}