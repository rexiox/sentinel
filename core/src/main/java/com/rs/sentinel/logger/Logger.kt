package com.rs.sentinel.logger

import com.rs.sentinel.report.SecurityReport
import com.rs.sentinel.violation.SecurityViolation

class Logger {

    fun log(
        report: SecurityReport,
        logger: (String) -> Unit,
    ) {
        logger("--- Sentinel Security Report ---")
        logger("Severity: ${report.severity}")
        logger("Risk Level: ${report.riskLevel}")
        logger("Threat Count: ${report.threats.size}")
        logger("Timestamp: ${report.timestamp}")

        if (report.threats.isEmpty()) {
            logger("No threats detected.")
            return
        }

        logger("")
        logger("--- Threat Breakdown ---")

        val grouped = report.threats.groupBy { threat -> getDynamicGroupName(threat.violation) }

        grouped.forEach { (groupName, threats) ->
            val groupSeverity = threats.sumOf { it.violation.severity }
            logger("$groupName (Total Severity: $groupSeverity)")

            threats.forEachIndexed { index, threat ->
                val threatName = threat.violation::class.simpleName ?: "Unknown"
                val severity = threat.violation.severity
                logger(" - ${index + 1}. $threatName (Severity: $severity)")
            }

            logger("")
        }
    }

    private fun getDynamicGroupName(violation: SecurityViolation): String {
        var cls: Class<*>? = violation.javaClass

        while (cls != null && cls != SecurityViolation::class.java) {
            val parent = cls.enclosingClass ?: cls.superclass

            if (parent != null && parent != SecurityViolation::class.java &&
                SecurityViolation::class.java.isAssignableFrom(parent) &&
                parent != cls
            ) {
                return parent.simpleName
            }

            cls = parent
        }

        return "Unknown"
    }
}