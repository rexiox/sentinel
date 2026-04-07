package sentinel.core.violation

abstract class SecurityViolation(
    open val severity: Int,
    open val detail: String?,
)