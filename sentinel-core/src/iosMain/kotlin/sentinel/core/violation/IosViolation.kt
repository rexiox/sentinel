package sentinel.core.violation

sealed class IosViolation(
    override val severity: Int,
) : SecurityViolation(severity = severity) {

    sealed class Jailbreak(severity: Int) : IosViolation(severity = severity) {

        data class AppInstalled(val appId: String? = null) : Jailbreak(severity = 90)

        data class URLSchemes(val urlScheme: String? = null) : Jailbreak(severity = 90)

        data class SuspiciousSymlinks(val path: String? = null) : Jailbreak(severity = 50)
    }

    sealed class Tamper(severity: Int) : IosViolation(severity = severity) {

        object SignatureMismatch : Tamper(severity = 100)
    }

    sealed class Hook(severity: Int) : IosViolation(severity = severity) {

        data class FrameworkDetected(val name: String? = null) : Hook(severity = 90)
    }

    sealed class Simulator(severity: Int) : IosViolation(severity = severity) {

        data class Detected(val name: String? = null) : Simulator(severity = 30)
    }

    sealed class Debugger(severity: Int) : IosViolation(severity = severity) {

        object Debuggable : Debugger(severity = 30)
    }

    sealed class Location(severity: Int) : IosViolation(severity = severity) {

        data class MockAppInstalled(val packages: List<String>) : Location(severity = 40)
    }
}