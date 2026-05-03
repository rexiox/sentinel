package sentinel.kit.detector

import android.content.Context
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.handler.ExceptionHandler
import sentinel.core.violation.AndroidViolation

open class RootDetector(
    private val context: Context,
) : SecurityDetector {

    init {
        ExceptionHandler.safely(context = "RootDetector.init") {
            loadLibrary()
        }
    }

    open fun loadLibrary() {
        System.loadLibrary("sentinel-root")
    }

    open external fun checkApps(context: Context): Boolean

    open external fun checkBinaries(): Boolean

    open external fun checkMounts(): Boolean

    open external fun checkSuCommand(): Boolean

    override fun detect(): List<Threat> = buildList {
        if (checkApps(context = context)) {
            add(element = Threat(violation = AndroidViolation.Root.AppInstalled()))
        }

        if (checkBinaries()) {
            add(element = Threat(violation = AndroidViolation.Root.SuBinaryFound))
        }

        if (checkMounts()) {
            add(element = Threat(violation = AndroidViolation.Root.SuspiciousMount()))
        }

        if (checkSuCommand()) {
            add(element = Threat(violation = AndroidViolation.Root.SuCommandExecuted))
        }
    }
}