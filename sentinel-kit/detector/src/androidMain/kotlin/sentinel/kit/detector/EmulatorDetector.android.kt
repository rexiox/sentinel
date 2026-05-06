package sentinel.kit.detector

import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.handler.ExceptionHandler
import sentinel.core.violation.AndroidViolation

open class EmulatorDetector : SecurityDetector {

    init {
        ExceptionHandler.safely(context = "EmulatorDetector.init") {
            loadLibrary()
        }
    }

    open fun loadLibrary() {
        System.loadLibrary("sentinel-emulator")
    }

    open external fun getEmulatorDetectionReason(): String?

    override fun detect(): List<Threat> {
        val reason = getEmulatorDetectionReason()

        return when {
            !reason.isNullOrBlank() -> {
                listOf(Threat(violation = AndroidViolation.Emulator.Detected(name = reason)))
            }

            else -> emptyList()
        }
    }
}