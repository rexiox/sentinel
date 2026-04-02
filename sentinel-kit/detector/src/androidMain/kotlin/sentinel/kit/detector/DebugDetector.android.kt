package sentinel.kit.detector

import android.content.Context
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.AndroidViolation

class DebugDetector(
    private val context: Context,
) : SecurityDetector {

    init {
        System.loadLibrary("sentinel-debugger")
    }

    external fun isDebuggerAttached(): Boolean

    external fun isPackageDebuggable(context: Context): Boolean

    external fun checkTestKeys(): Boolean

    override fun detect(): List<Threat> = buildList {
        if (isDebuggerAttached()) {
            add(element = Threat(violation = AndroidViolation.Debugger.DebuggerAttached))
        }

        if (isPackageDebuggable(context = context)) {
            add(element = Threat(violation = AndroidViolation.Debugger.Debuggable))
        }

        if (checkTestKeys()) {
            add(element = Threat(violation = AndroidViolation.Debugger.TestKeys))
        }
    }
}