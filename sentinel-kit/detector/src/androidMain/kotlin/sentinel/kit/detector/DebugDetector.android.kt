package sentinel.kit.detector

import android.content.Context
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.handler.ExceptionHandler
import sentinel.core.violation.AndroidViolation

open class DebugDetector(
    private val context: Context,
) : SecurityDetector {

    init {
        ExceptionHandler.safely(context = "DebugDetector.init") {
            loadLibrary()
        }
    }

    open fun loadLibrary() {
        System.loadLibrary("sentinel-debugger")
    }

    open external fun isDebuggerAttached(): Boolean

    open external fun isPackageDebuggable(context: Context): Boolean

    open external fun checkTestKeys(): Boolean

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