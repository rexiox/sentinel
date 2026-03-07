package sentinel.kit.detector

import android.content.Context
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.AndroidViolation

class RootDetector(
    private val context: Context,
) : SecurityDetector {

    init {
        System.loadLibrary("sentinel-root")
    }

    external fun checkApps(context: Context): Boolean

    external fun checkBinaries(): Boolean

    external fun checkMounts(): Boolean

    external fun checkSuCommand(): Boolean

    override fun detect(): List<Threat> = buildList {
        if (checkApps(context = context)) {
            add(
                Threat(
                    violation = AndroidViolation.Root.AppInstalled()
                )
            )
        }

        if (checkBinaries()) {
            add(
                Threat(
                    violation = AndroidViolation.Root.SuBinaryFound
                )
            )
        }

        if (checkMounts()) {
            add(
                Threat(
                    violation = AndroidViolation.Root.SuspiciousMount()
                )
            )
        }

        if (checkSuCommand()) {
            add(
                Threat(
                    violation = AndroidViolation.Root.SuCommandExecuted
                )
            )
        }
    }
}