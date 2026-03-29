package sentinel.kit.detector

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.dlsym
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.IosViolation
import sentinel.detector.checkDyldImages
import sentinel.detector.checkFridaDefaultPort
import sentinel.detector.isFunctionHooked
import sentinel.detector.scanMemoryForFridaSignatures
import sentinel.kit.detector.constant.DetectorConst

class HookDetector : SecurityDetector {

    @OptIn(ExperimentalForeignApi::class)
    override fun detect(): List<Threat> = buildList {
        checkDyldImages()?.also { name ->
            add(
                element = Threat(
                    violation = IosViolation.Hook.FrameworkDetected(name = name.toKString())
                )
            )
            platform.posix.free(name)
        }

        if (scanMemoryForFridaSignatures() || checkFridaDefaultPort()) {
            add(element = Threat(violation = IosViolation.Hook.FrameworkDetected(name = "Frida")))
        }

        DetectorConst.CRITICAL_SYSTEM_FUNCTIONS.forEach { funcName ->
            val funcAddr = dlsym(__handle = null, __symbol = funcName)

            if (funcAddr != null && isFunctionHooked(func_ptr = funcAddr)) {
                add(
                    element = Threat(
                        violation = IosViolation.Hook.InlineHookDetected(name = funcName)
                    )
                )
            }
        }
    }
}