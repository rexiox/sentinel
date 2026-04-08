package sentinel.core.logger

import sentinel.core.report.AndroidSecurityReport
import sentinel.core.report.SecurityReport
import sentinel.core.violation.AndroidViolation
import sentinel.core.violation.getGroupName

actual object SentinelLogger {

    actual fun info(tag: String, msg: String) {
        println("[$tag] $msg")
    }

    actual fun report(report: SecurityReport) {
        if (report is AndroidSecurityReport) {
            println("╔══════════════════════════════════════════════════════")
            println("║ SENTINEL ANDROID SECURITY REPORT")
            println("╠══════════════════════════════════════════════════════")
            println("║ Risk Level: ${report.riskLevel}")
            println("║ Total Risk Score: ${report.severity} / ${report.threshold}")
            println("║ Summary: [Root: ${report.isRooted}, Debugged: ${report.isDebugged}]")
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
                            is AndroidViolation.Root.AppInstalled -> "Package: ${violation.detail.orEmpty()}"
                            is AndroidViolation.Root.SuspiciousMount -> "Mount: ${violation.detail.orEmpty()}"
                            is AndroidViolation.Hook.FrameworkDetected -> "Framework: ${violation.detail ?: "Unknown"}"
                            is AndroidViolation.Emulator.Detected -> "Name: ${violation.detail ?: "Unknown"}"
                            is AndroidViolation.Location.MockAppInstalled -> "Apps: ${violation.detail}"
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