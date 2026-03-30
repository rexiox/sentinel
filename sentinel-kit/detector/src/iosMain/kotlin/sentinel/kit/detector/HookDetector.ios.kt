package sentinel.kit.detector

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.RTLD_DEFAULT
import platform.posix.dlsym
import platform.posix.free
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
    private val systemFunctionAddresses: Map<String, COpaquePointer?> by lazy {
        DetectorConst.CRITICAL_SYSTEM_FUNCTIONS.associateWith { funcName ->
            dlsym(__handle = RTLD_DEFAULT, __symbol = funcName)
        }
    }

    @OptIn(ExperimentalForeignApi::class)
    override fun detect(): List<Threat> = buildList {
        checkDyldImages()?.also { name ->
            add(
                element = Threat(
                    violation = IosViolation.Hook.FrameworkDetected(name = name.toKString())
                )
            )
            free(name)
        }

        if (scanMemoryForFridaSignatures()) {
            add(
                element = Threat(
                    violation = IosViolation.Hook.FrameworkDetected(name = "Frida-Memory")
                )
            )
        }

        if (checkFridaDefaultPort()) {
            add(
                element = Threat(
                    violation = IosViolation.Hook.FrameworkDetected(name = "Frida-Port")
                )
            )
        }

        systemFunctionAddresses.forEach { (funcName, funcAddr) ->
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