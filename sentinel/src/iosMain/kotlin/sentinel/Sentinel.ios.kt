package sentinel

import kotlinx.coroutines.runBlocking
import sentinel.core.detector.SecurityDetector
import sentinel.core.identity.Identity
import sentinel.core.logger.SentinelLogger
import sentinel.core.report.IosSecurityReport
import sentinel.core.report.SecurityReport
import sentinel.kit.runtime.HookRuntime
import sentinel.runtime.InstallScope
import sentinel.runtime.Runtime

actual class Sentinel internal constructor(
    private val detectors: List<SecurityDetector>,
    actual val config: Config,
) {

    init {
        HookRuntime.initialize()
    }

    actual fun runtime(block: InstallScope.() -> Unit) {
        Runtime.activate(
            block = block,
            provider = {
                runBlocking {
                    inspect()
                }
            }
        )
    }

    actual suspend fun inspect(): SecurityReport = IosSecurityReport(
        threats = detectors.flatMap { detector -> detector.detect().orEmpty() },
        threshold = config.threshold
    ).also { report ->
        if (config.isLoggingEnabled) {
            SentinelLogger.report(report = report)
        }
    }

    companion object {

        lateinit var Identity: Identity
            internal set
    }
}