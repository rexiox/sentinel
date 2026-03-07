package sentinel.core.violation

expect fun getViolations(): List<SecurityViolation>

expect fun getGroupName(violation: SecurityViolation): String