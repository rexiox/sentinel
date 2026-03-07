package sentinel.core.violation

actual fun getViolations(): List<SecurityViolation> = listOf(
    AndroidViolation.Root.AppInstalled(),
    AndroidViolation.Root.SuBinaryFound,
    AndroidViolation.Root.SuCommandExecuted,
    AndroidViolation.Root.SuspiciousMount(),

    AndroidViolation.Tamper.PackageNameChanged,
    AndroidViolation.Tamper.DexIntegrityFailed,
    AndroidViolation.Tamper.SignatureMismatch,

    AndroidViolation.Hook.FrameworkDetected(),
    AndroidViolation.Hook.FridaDetected,

    AndroidViolation.Emulator.Detected(),

    AndroidViolation.Debugger.Debuggable,
    AndroidViolation.Debugger.TestKeys,

    AndroidViolation.Location.MockSettingEnabled,
    AndroidViolation.Location.MockLocationDetected,
    AndroidViolation.Location.MockAppInstalled(packages = emptyList())
)

actual fun getGroupName(violation: SecurityViolation): String = when (violation) {
    is AndroidViolation.Root -> "Root"
    is AndroidViolation.Tamper -> "Tamper"
    is AndroidViolation.Hook -> "Hook"
    is AndroidViolation.Emulator -> "Emulator"
    is AndroidViolation.Debugger -> "Debugger"
    is AndroidViolation.Location -> "Location"
    else -> "General"
}