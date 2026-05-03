package sentinel.core.logger

import sentinel.core.report.SecurityReport

const val SENTINEL_TAG = "Sentinel"

expect object SentinelLogger {

    fun print(tag: String = SENTINEL_TAG, msg: Any)

    fun print(tag: String = SENTINEL_TAG, msg: Any, throwable: Throwable)

    fun report(report: SecurityReport)
}