package sentinel

import sentinel.core.report.SecurityReport

expect class Sentinel {

    val config: Config

    fun inspect(): SecurityReport

    fun report(report: SecurityReport)
}

