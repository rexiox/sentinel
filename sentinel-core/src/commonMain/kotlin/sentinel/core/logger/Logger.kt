package sentinel.core.logger

import sentinel.core.report.SecurityReport

expect object SentinelLogger {

    fun print(tag: String = "Sentinel", msg: Any)

    fun report(report: SecurityReport)
}