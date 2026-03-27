package sentinel.core.violation

sealed class AndroidViolation(
    override val severity: Int,
) : SecurityViolation(severity = severity) {

    sealed class Root(severity: Int) : AndroidViolation(severity = severity) {

        data class AppInstalled(val packageName: String? = null) : Root(severity = 50)

        data class SuspiciousMount(val mountPoint: String? = null) : Root(severity = 60)

        object SuBinaryFound : Root(severity = 85)

        object SuCommandExecuted : Root(severity = 85)
    }

    sealed class Tamper(severity: Int) : AndroidViolation(severity = severity) {

        object PackageNameChanged : Tamper(severity = 85)

        object DexIntegrityFailed : Tamper(severity = 95)

        object SignatureMismatch : Tamper(severity = 100)
    }

    sealed class Hook(severity: Int) : AndroidViolation(severity = severity) {

        data class FrameworkDetected(val name: String? = null) : Hook(severity = 90)

        object FridaDetected : Hook(severity = 100)
    }

    sealed class Emulator(severity: Int) : AndroidViolation(severity = severity) {

        data class Detected(val name: String? = null) : Emulator(severity = 30)
    }

    sealed class Debugger(severity: Int) : AndroidViolation(severity = severity) {

        object Debuggable : Debugger(severity = 40)

        object TestKeys : Debugger(severity = 40)
    }

    sealed class Location(severity: Int) : AndroidViolation(severity = severity) {

        object MockSettingEnabled : Location(severity = 50)

        object MockLocationDetected : Location(severity = 95)

        data class MockAppInstalled(val packages: List<String>) : Location(severity = 70)
    }
}