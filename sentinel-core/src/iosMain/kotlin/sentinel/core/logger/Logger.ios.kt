package sentinel.core.logger

import sentinel.core.report.IosSecurityReport
import sentinel.core.report.SecurityReport
import sentinel.core.violation.IosViolation
import sentinel.core.violation.getGroupName

actual object SentinelLogger {

    actual fun print(tag: String, msg: Any) {
        println("[$tag] $msg")
    }

    actual fun report(report: SecurityReport) {
        if (report is IosSecurityReport) {
            println("╔══════════════════════════════════════════════════════")
            println("║ SENTINEL IOS SECURITY REPORT")
            println("╠══════════════════════════════════════════════════════")
            println("║ Risk Level: ${report.riskLevel}")
            println("║ Total Risk Score: ${report.severity} / ${report.threshold}")
            println("║ Summary: [Jailbroken: ${report.isJailbroken}, Debugged: ${report.isDebugged}]")
            println("╠══════════════════════════════════════════════════════")

            if (report.threats.isEmpty()) {
                println("║ No threats detected. Device is secure.")
            } else {
                println("║ DETECTED THREATS (${report.threats.size})")

                val groupedThreats = report.threats.groupBy { threat ->
                    getGroupName(threat.violation)
                }

                groupedThreats.forEach { (group, threats) ->
                    println("║")
                    println("║ Violation: $group")

                    threats.forEachIndexed { index, threat ->
                        val violation = threat.violation
                        val name = violation::class.simpleName ?: "Unknown Violation"

                        val detail = when (violation) {
                            is IosViolation.Jailbreak.AppInstalled -> "App Id: ${violation.detail.orEmpty()}"
                            is IosViolation.Jailbreak.URLSchemes -> "URL Scheme: ${violation.detail.orEmpty()}"
                            is IosViolation.Jailbreak.SuspiciousSymlinks -> "Path: ${violation.detail.orEmpty()}"
                            is IosViolation.Hook.FrameworkDetected -> "Framework: ${violation.detail ?: "Unknown"}"
                            is IosViolation.Simulator.Detected -> "Name: ${violation.detail ?: "Unknown"}"
                            is IosViolation.Location.MockAppInstalled -> "Apps: ${violation.detail}"
                            else -> "System integrity check failed"
                        }

                        println("║   ${index + 1}. $name")
                        println("║      Severity: ${violation.severity}")
                        println("║      Detail: $detail")
                    }
                }
            }

            println("╚══════════════════════════════════════════════════════")
        }
    }
}