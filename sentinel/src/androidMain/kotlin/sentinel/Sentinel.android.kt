package sentinel

import sentinel.core.detector.SecurityDetector
import sentinel.core.identity.Identity
import sentinel.core.logger.logReport
import sentinel.core.report.AndroidSecurityReport
import sentinel.core.report.SecurityReport

actual class Sentinel internal constructor(
    private val detectors: List<SecurityDetector>,
    actual val config: Config,
) {
    actual fun inspect(): SecurityReport = AndroidSecurityReport(
        threats = detectors.flatMap { detector -> detector.detect().orEmpty() },
        threshold = config.threshold
    ).also { report ->
        if (config.isLoggingEnabled) {
            report(report = report)
        }
    }

    actual fun report(report: SecurityReport) = logReport(report = report)

    companion object {

        lateinit var Identity: Identity
            internal set
    }
}