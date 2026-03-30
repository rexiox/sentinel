package sentinel.ui.violation

import sentinel.core.violation.AndroidViolation
import sentinel.core.violation.SecurityViolation

actual fun getViolations(): List<SecurityViolation> = listOf(
    AndroidViolation.Root.SuBinaryFound,
    AndroidViolation.Root.SuCommandExecuted,
    AndroidViolation.Root.SuspiciousMount(),
    AndroidViolation.Root.AppInstalled(),

    AndroidViolation.Tamper.PackageNameChanged,
    AndroidViolation.Tamper.DexIntegrityFailed,
    AndroidViolation.Tamper.SignatureMismatch,

    AndroidViolation.Hook.FrameworkDetected(),

    AndroidViolation.Emulator.Detected(),

    AndroidViolation.Debugger.Debuggable,
    AndroidViolation.Debugger.TestKeys,

    AndroidViolation.Location.MockSettingEnabled,
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