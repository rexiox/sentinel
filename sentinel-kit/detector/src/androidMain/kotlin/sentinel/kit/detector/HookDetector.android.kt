package sentinel.kit.detector

import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.AndroidViolation

class HookDetector : SecurityDetector {

    init {
        System.loadLibrary("sentinel-hook")
    }

    external fun isFridaDetected(): Boolean

    external fun checkStackTraceManually(): String?

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