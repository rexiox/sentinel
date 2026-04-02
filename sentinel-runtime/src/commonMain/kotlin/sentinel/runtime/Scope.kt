package sentinel.runtime

import sentinel.core.report.SecurityReport
import sentinel.core.violation.SecurityViolation

open class InstallScope {
    internal var onCompromised: (() -> Unit)? = null
    internal var onTampered: (() -> Unit)? = null
    internal var onHooked: (() -> Unit)? = null
    internal var onSimulated: (() -> Unit)? = null
    internal var onDebugged: (() -> Unit)? = null
    internal var onCritical: ((Int) -> Unit)? = null
    internal var onSafe: (() -> Unit)? = null

    fun onCompromised(block: () -> Unit) { onCompromised = block }
    fun onTampered(block: () -> Unit) { onTampered = block }
    fun onHooked(block: () -> Unit) { onHooked = block }
    fun onSimulated(block: () -> Unit) { onSimulated = block }
    fun onDebugged(block: () -> Unit) { onDebugged = block }
    fun onCritical(block: (Int) -> Unit) { onCritical = block }
    fun onSafe(block: () -> Unit) { onSafe = block }
}

expect class Scope() : InstallScope {

    internal fun dispatch(violation: SecurityViolation, report: SecurityReport)
}