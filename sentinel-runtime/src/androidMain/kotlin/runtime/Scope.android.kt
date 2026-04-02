package sentinel.runtime

import sentinel.core.report.SecurityReport
import sentinel.core.violation.AndroidViolation
import sentinel.core.violation.SecurityViolation

actual class Scope : InstallScope() {

    actual fun dispatch(violation: SecurityViolation, report: SecurityReport) {
        when (violation) {
            is AndroidViolation.Root -> onCompromised?.invoke()
            is AndroidViolation.Hook -> onHooked?.invoke()
            is AndroidViolation.Emulator -> onSimulated?.invoke()
            is AndroidViolation.Debugger -> onDebugged?.invoke()
            is AndroidViolation.Tamper -> onTampered?.invoke()
        }

        when {
            report.isCritical() -> onCritical?.invoke(report.severity)
            report.isSafe() -> onSafe?.invoke()
        }
    }
}