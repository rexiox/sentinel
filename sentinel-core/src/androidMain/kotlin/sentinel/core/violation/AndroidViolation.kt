package sentinel.core.violation

sealed class AndroidViolation(
    override val severity: Int,
) : SecurityViolation(severity = severity) {

    sealed class Root(severity: Int) : AndroidViolation(severity = severity) {

        data class AppInstalled(val packageName: String? = null) : Root(severity = 65)

        data class SuspiciousMount(val mountPoint: String? = null) : Root(severity = 75)

        object SuBinaryFound : Root(severity = 90)

        object SuCommandExecuted : Root(severity = 100)
    }

    sealed class Tamper(severity: Int) : AndroidViolation(severity = severity) {

        object PackageNameChanged : Tamper(severity = 90)

        object DexIntegrityFailed : Tamper(severity = 95)

        object SignatureMismatch : Tamper(severity = 100)
    }

    sealed class Hook(severity: Int) : AndroidViolation(severity = severity) {

        data class FrameworkDetected(val name: String? = null) : Hook(severity = 100)
    }

    sealed class Emulator(severity: Int) : AndroidViolation(severity = severity) {

        data class Detected(val name: String? = null) : Emulator(severity = 25)
    }

    sealed class Debugger(severity: Int) : AndroidViolation(severity = severity) {

        object Debuggable : Debugger(severity = 50)

        object TestKeys : Debugger(severity = 25)
    }

    sealed class Location(severity: Int) : AndroidViolation(severity = severity) {

        object MockSettingEnabled : Location(severity = 50)

        data class MockAppInstalled(val packages: List<String>) : Location(severity = 60)
    }
}