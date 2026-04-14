package sentinel

import kotlinx.coroutines.runBlocking
import sentinel.core.detector.SecurityDetector
import sentinel.core.identity.Identity
import sentinel.core.logger.SentinelLogger
import sentinel.core.report.AndroidSecurityReport
import sentinel.core.report.SecurityReport
import sentinel.kit.runtime.DebugRuntime
import sentinel.kit.runtime.HookRuntime
import sentinel.kit.runtime.RootRuntime
import sentinel.runtime.SecurityScope
import sentinel.runtime.Runtime

/**
 * [Sentinel] is the primary entry point for the security framework, responsible for orchestrating
 * threat detection across the application runtime.
 *
 * This class initializes essential security runtimes—[RootRuntime], [HookRuntime], and [DebugRuntime]—
 * to monitor the environment for indicators of compromise (IoC) such as root access,
 * code hooking, or active debugging sessions.
 *
 * @property detectors A collection of [SecurityDetector] instances used to evaluate specific security threats.
 * @property config The [Config] instance defining the operational behavior and thresholds for the security suite.
 */
actual class Sentinel internal constructor(
    private val detectors: List<SecurityDetector>,
    actual val config: Config,
) {

    /**
     * Initializes the core security runtimes.
     * * This block triggers checks for:
     * - **Root:** Detecting unauthorized elevated system privileges.
     * - **Hooking:** Identifying frameworks (e.g., Frida, Xposed) attempting to intercept runtime execution.
     * - **Debugging:** Monitoring for active debuggers attached to the process.
     */
    init {
        RootRuntime.initialize()
        HookRuntime.initialize()
        DebugRuntime.initialize()
    }

    /**
     * Activates a [SecurityScope] to define a region of protected code execution.
     *
     * @param block The lambda function containing the application logic to be executed within the
     * security-aware scope. The runtime will automatically trigger an [inspect] call before
     * proceeding with the block.
     */
    actual fun runtime(block: SecurityScope.() -> Unit) {
        Runtime.activate(
            block = block,
            provider = {
                runBlocking {
                    inspect()
                }
            }
        )
    }

    /**
     * Performs a comprehensive security inspection by aggregating results from all configured detectors.
     *
     * This method executes the detection logic defined in [detectors], collects the security threats,
     * and generates an [AndroidSecurityReport]. The report is filtered and processed based on the
     * sensitivity levels defined in [Config.threshold].
     *
     * If [Config.isLoggingEnabled] is set to true, the resulting report is automatically
     * forwarded to the [SentinelLogger].
     *
     * @return A [SecurityReport] containing the consolidated findings of the inspection.
     * @see AndroidSecurityReport
     * @see Config.threshold
     */
    actual suspend fun inspect(): SecurityReport = AndroidSecurityReport(
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