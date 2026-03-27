package sentinel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import sentinel.core.guard.SentinelGuard
import sentinel.core.report.SecurityReport

suspend fun Sentinel.scan(
    intervalMs: Long = 30_000,
    block: SentinelGuard.() -> Unit,
) {
    asSecurityFlow(intervalMs = intervalMs).collect { report ->
        SentinelGuard(report = report).apply(block)
    }
}

private fun Sentinel.asSecurityFlow(
    intervalMs: Long = 30_000,
): Flow<SecurityReport> = flow {
    while (currentCoroutineContext().isActive) {
        emit(inspect())
        delay(intervalMs)
    }
}.flowOn(Dispatchers.Default)