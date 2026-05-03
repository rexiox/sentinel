package sentinel.kit.detector

import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.handler.ExceptionHandler
import sentinel.core.violation.AndroidViolation

open class HookDetector : SecurityDetector {

    init {
        ExceptionHandler.safely(context = "HookDetector.init") {
            loadLibrary()
        }
    }

    open fun loadLibrary() {
        System.loadLibrary("sentinel-hook")
    }

    open external fun isFridaDetected(): Boolean

    open external fun checkStackTraceManually(): String?

    override fun detect(): List<Threat> {
        val name = checkStackTraceManually()

        return buildList {
            if (isFridaDetected()) {
                add(element = Threat(violation = AndroidViolation.Hook.FrameworkDetected(name = "Frida")))
            }

            if (name != null) {
                add(element = Threat(violation = AndroidViolation.Hook.FrameworkDetected(name = name)))
            }
        }
    }
}