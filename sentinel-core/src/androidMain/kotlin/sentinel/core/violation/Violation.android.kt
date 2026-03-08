package sentinel.core.violation

actual fun getGroupName(violation: SecurityViolation): String = when (violation) {
    is AndroidViolation.Root -> "Root"
    is AndroidViolation.Tamper -> "Tamper"
    is AndroidViolation.Hook -> "Hook"
    is AndroidViolation.Emulator -> "Emulator"
    is AndroidViolation.Debugger -> "Debugger"
    is AndroidViolation.Location -> "Location"
    else -> "General"
}