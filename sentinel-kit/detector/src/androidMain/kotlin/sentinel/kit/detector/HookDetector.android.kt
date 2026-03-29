package sentinel.kit.detector

import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.AndroidViolation
import sentinel.kit.detector.constant.DetectorConst

class HookDetector : SecurityDetector {

    init {
        System.loadLibrary("sentinel-hook")
    }

    private external fun isFridaDetected(): Boolean

    override fun detect(): List<Threat> {
        val (isCheckStackTraceManually, name) = checkStackTraceManually()

        return buildList {
            if (isFridaDetected()) {
                add(element = Threat(violation = AndroidViolation.Hook.FrameworkDetected(name = "Frida")))
            }

            if (isCheckStackTraceManually) {
                add(element = Threat(violation = AndroidViolation.Hook.FrameworkDetected(name = name)))
            }
        }
    }

    private fun checkStackTraceManually(): Pair<Boolean, String?> = runCatching {
        throw Exception()
    }.onFailure { exception ->
        val detectedPackage = exception.stackTrace.firstNotNullOfOrNull { element ->
            DetectorConst.HOOK_PACKAGES.firstOrNull(predicate = element.className::contains)
        }
        (detectedPackage != null) to detectedPackage
    }.getOrElse {
        false to null
    }
}