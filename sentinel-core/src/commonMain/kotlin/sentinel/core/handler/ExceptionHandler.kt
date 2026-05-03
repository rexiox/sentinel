package sentinel.core.handler

import sentinel.core.logger.SentinelLogger

object ExceptionHandler {

    inline fun <T> safely(context: String, block: () -> T): T? = try {
        block()
    } catch (e: Exception) {
        SentinelLogger.print(msg = "Error in $context", throwable = e)
        null
    }
}