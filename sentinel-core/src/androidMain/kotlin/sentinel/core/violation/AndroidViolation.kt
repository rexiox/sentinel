package sentinel.core.violation

sealed class AndroidViolation(
    override val severity: Int,
) : SecurityViolation(severity = severity) {

    sealed class Root(severity: Int) : AndroidViolation(severity = severity) {

        data class AppInstalled(val packageName: String? = null) : Root(severity = 90)

        object SuBinaryFound : Root(severity = 85)

        object SuCommandExecuted : Root(severity = 85)

        data class SuspiciousMount(val mountPoint: String? = null) : Root(severity = 80)
    }

    sealed class Tamper(severity: Int) : AndroidViolation(severity = severity) {

        object PackageNameChanged : Tamper(severity = 80)

        object DexIntegrityFailed : Tamper(severity = 90)

        object SignatureMismatch : Tamper(severity = 100)
    }

    sealed class Hook(severity: Int) : AndroidViolation(severity = severity) {

        data class FrameworkDetected(val name: String? = null) : Hook(severity = 90)

        object FridaDetected : Hook(severity = 95)
    }

    sealed class Emulator(severity: Int) : AndroidViolation(severity = severity) {

        data class Detected(val name: String? = null) : Emulator(severity = 30)
    }

    sealed class Debugger(severity: Int) : AndroidViolation(severity = severity) {

        object Debuggable : Debugger(severity = 30)

        object TestKeys : Debugger(severity = 30)
    }

    sealed class Location(severity: Int) : AndroidViolation(severity = severity) {

        object MockSettingEnabled : Location(severity = 60)

        object MockLocationDetected : Location(severity = 80)

        data class MockAppInstalled(val packages: List<String>) : Location(severity = 40)
    }
}