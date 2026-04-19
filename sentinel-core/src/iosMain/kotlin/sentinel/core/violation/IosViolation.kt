package sentinel.core.violation

sealed class IosViolation(
    override val severity: Int,
    override val detail: String? = null,
) : SecurityViolation(severity = severity, detail = detail) {

    sealed class Jailbreak(severity: Int) : IosViolation(severity = severity) {

        data class Sandbox(val appId: String? = null) : Jailbreak(severity = 100) {
            override val detail: String? = appId
        }

        data class SystemPaths(val path: String? = null) : Jailbreak(severity = 85) {
            override val detail: String? = path
        }

        data class SuspiciousSymlinks(val path: String? = null) : Jailbreak(severity = 80) {
            override val detail: String? = path
        }

        data class AppInstalled(val appId: String? = null) : Jailbreak(severity = 70) {
            override val detail: String? = appId
        }

        data class URLSchemes(val urlScheme: String? = null) : Jailbreak(severity = 35) {
            override val detail: String? = urlScheme
        }
    }

    sealed class Tamper(severity: Int) : IosViolation(severity = severity) {

        object BundleIdChanged : Tamper(severity = 90)

        object ProvisioningHashMismatch : Tamper(severity = 100)
    }

    sealed class Hook(severity: Int) : IosViolation(severity = severity) {

        object Detected : Hook(severity = 0)

        data class FrameworkDetected(val name: String? = null) : Hook(severity = 100) {
            override val detail: String? = name
        }

        data class InlineHookDetected(val name: String? = null) : Hook(severity = 90) {
            override val detail: String? = name
        }
    }

    sealed class Simulator(severity: Int) : IosViolation(severity = severity) {

        data class Detected(val name: String? = null) : Simulator(severity = 25)
    }

    sealed class Debugger(severity: Int) : IosViolation(severity = severity) {

        object DebuggerAttached : Debugger(severity = 90)
    }

    sealed class Location(severity: Int) : IosViolation(severity = severity) {

        data class MockAppInstalled(val packages: List<String>) : Location(severity = 60) {
            override val detail: String = packages.toString()
        }
    }
}