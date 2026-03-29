package sentinel.core.violation

sealed class IosViolation(
    override val severity: Int,
) : SecurityViolation(severity = severity) {

    sealed class Jailbreak(severity: Int) : IosViolation(severity = severity) {

        data class Sandbox(val appId: String? = null) : Jailbreak(severity = 100)

        data class SuspiciousSymlinks(val path: String? = null) : Jailbreak(severity = 85)

        data class AppInstalled(val appId: String? = null) : Jailbreak(severity = 70)

        data class URLSchemes(val urlScheme: String? = null) : Jailbreak(severity = 35)
    }

    sealed class Tamper(severity: Int) : IosViolation(severity = severity) {

        object BundleIdChanged : Tamper(severity = 90)

        object SignatureMismatch : Tamper(severity = 100)
    }

    sealed class Hook(severity: Int) : IosViolation(severity = severity) {

        data class FrameworkDetected(val name: String? = null) : Hook(severity = 100)

        data class InlineHookDetected(val name: String? = null) : Hook(severity = 90)
    }

    sealed class Simulator(severity: Int) : IosViolation(severity = severity) {

        data class Detected(val name: String? = null) : Simulator(severity = 25)
    }

    sealed class Debugger(severity: Int) : IosViolation(severity = severity) {

        object Debuggable : Debugger(severity = 50)
    }

    sealed class Location(severity: Int) : IosViolation(severity = severity) {

        data class MockAppInstalled(val packages: List<String>) : Location(severity = 60)
    }
}