package sentinel.ui.violation

import sentinel.core.violation.IosViolation
import sentinel.core.violation.SecurityViolation

actual fun getViolations(): List<SecurityViolation> = listOf(
    IosViolation.Jailbreak.Sandbox(),
    IosViolation.Jailbreak.SuspiciousSymlinks(),
    IosViolation.Jailbreak.AppInstalled(),
    IosViolation.Jailbreak.URLSchemes(),

    IosViolation.Tamper.BundleIdChanged,
    IosViolation.Tamper.SignatureMismatch,

    IosViolation.Hook.FrameworkDetected(),
    IosViolation.Hook.InlineHookDetected(),

    IosViolation.Simulator.Detected(),

    IosViolation.Debugger.Debuggable,

    // IosViolation.Location.MockAppInstalled(packages = emptyList())
)

actual fun getGroupName(violation: SecurityViolation): String = when (violation) {
    is IosViolation.Jailbreak -> "Jailbreak"
    is IosViolation.Tamper -> "Tamper"
    is IosViolation.Hook -> "Hook"
    is IosViolation.Simulator -> "Simulator"
    is IosViolation.Debugger -> "Debugger"
    // is IosViolation.Location -> "Location"
    else -> "General"
}