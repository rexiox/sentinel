package sentinel

import sentinel.core.report.SecurityReport

expect class Sentinel {

    val config: Config

    suspend fun inspect(): SecurityReport

    fun report(report: SecurityReport)
}

