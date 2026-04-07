package sentinel.core.violation

sealed class AndroidViolation(
    override val severity: Int,
    override val detail: String? = null,
) : SecurityViolation(severity = severity, detail = detail) {

    sealed class Root(severity: Int) : AndroidViolation(severity = severity) {

        object Detected : Root(severity = 0)

        data class AppInstalled(val packageName: String? = null) : Root(severity = 65) {
            override val detail: String? = packageName
        }

        data class SuspiciousMount(val mountPoint: String? = null) : Root(severity = 75) {
            override val detail: String? = mountPoint
        }

        object SuBinaryFound : Root(severity = 90)

        object SuCommandExecuted : Root(severity = 100)
    }

    sealed class Tamper(severity: Int) : AndroidViolation(severity = severity) {

        object Detected : Tamper(severity = 0)

        object PackageNameChanged : Tamper(severity = 90)

        object DexIntegrityFailed : Tamper(severity = 95)

        object SignatureMismatch : Tamper(severity = 100)
    }

    sealed class Hook(severity: Int) : AndroidViolation(severity = severity) {

        object Detected : Hook(severity = 0)

        data class FrameworkDetected(val name: String? = null) : Hook(severity = 100) {
            override val detail: String? = name
        }
    }

    sealed class Emulator(severity: Int) : AndroidViolation(severity = severity) {

        data class Detected(val name: String? = null) : Emulator(severity = 25) {
            override val detail: String? = name
        }
    }

    sealed class Debugger(severity: Int) : AndroidViolation(severity = severity) {

        object Detected : Debugger(severity = 0)

        object DebuggerAttached : Debugger(severity = 90)

        object Debuggable : Debugger(severity = 50)

        object TestKeys : Debugger(severity = 25)
    }

    sealed class Location(severity: Int) : AndroidViolation(severity = severity) {

        object MockSettingEnabled : Location(severity = 50)

        data class MockAppInstalled(val packages: List<String>) : Location(severity = 60) {
            override val detail: String? = packages.toString()
        }
    }
}