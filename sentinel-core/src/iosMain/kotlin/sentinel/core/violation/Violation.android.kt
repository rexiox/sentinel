package sentinel.core.violation

actual fun getViolations(): List<SecurityViolation> = listOf(
    IosViolation.Jailbreak.SuspiciousMount(),
    IosViolation.Jailbreak.AppInstalled(),

    IosViolation.Tamper.SignatureMismatch,

    IosViolation.Hook.FrameworkDetected(),

    IosViolation.Simulator.Detected(),

    IosViolation.Debugger.Debuggable,

    IosViolation.Location.MockAppInstalled(packages = emptyList())
)

actual fun getGroupName(violation: SecurityViolation): String = when (violation) {
    is IosViolation.Jailbreak -> "Jailbreak"
    is IosViolation.Tamper -> "Tamper"
    is IosViolation.Hook -> "Hook"
    is IosViolation.Simulator -> "Simulator"
    is IosViolation.Debugger -> "Debugger"
    is IosViolation.Location -> "Location"
    else -> "General"
}