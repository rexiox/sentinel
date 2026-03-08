package sentinel.core.violation

actual fun getGroupName(violation: SecurityViolation): String = when (violation) {
    is IosViolation.Jailbreak -> "Jailbreak"
    is IosViolation.Tamper -> "Tamper"
    is IosViolation.Hook -> "Hook"
    is IosViolation.Simulator -> "Simulator"
    is IosViolation.Debugger -> "Debugger"
    is IosViolation.Location -> "Location"
    else -> "General"
}