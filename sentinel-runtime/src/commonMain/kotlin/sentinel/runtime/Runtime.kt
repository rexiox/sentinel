package sentinel.runtime

import sentinel.core.report.SecurityReport
import sentinel.core.violation.SecurityViolation

object Runtime {

    private var activeScope: Scope? = null
    private var reportProvider: (() -> SecurityReport)? = null

    fun activate(
        block: InstallScope.() -> Unit,
        provider: () -> SecurityReport,
    ) {
        this.activeScope = Scope().apply(block)
        this.reportProvider = provider
    }

    fun onViolationDetected(violation: SecurityViolation) {
        val scope = activeScope ?: return
        val report = reportProvider?.invoke() ?: return

        scope.dispatch(
            violation = violation,
            report = report
        )
    }
}