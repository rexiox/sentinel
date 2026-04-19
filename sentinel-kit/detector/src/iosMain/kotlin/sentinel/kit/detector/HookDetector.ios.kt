@file:OptIn(ExperimentalForeignApi::class)

package sentinel.kit.detector

import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ExperimentalForeignApi
import platform.posix.RTLD_DEFAULT
import platform.posix.dlsym
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.IosViolation
import sentinel.detector.checkReservedPort
import sentinel.detector.isInstructionTampered
import sentinel.detector.scanMemorySignatures
import sentinel.detector.verifyLoadedImages
import sentinel.kit.detector.constant.DetectorConst

open class HookDetector : SecurityDetector {

    private val systemFunctionAddresses: Map<String, COpaquePointer?> by lazy {
        DetectorConst.CRITICAL_SYSTEM_FUNCTIONS.associateWith { funcName ->
            dlsym(__handle = RTLD_DEFAULT, __symbol = funcName)
        }
    }

    override fun detect(): List<Threat> = buildList {
        if (verifyLoadedImages()) {
            add(
                element = Threat(violation = IosViolation.Hook.FrameworkDetected(name = "Frida"))
            )
        }

        if (scanMemorySignatures()) {
            add(
                element = Threat(violation = IosViolation.Hook.FrameworkDetected(name = "Frida-Memory"))
            )
        }

        if (checkReservedPort()) {
            add(
                element = Threat(violation = IosViolation.Hook.FrameworkDetected(name = "Frida-Port"))
            )
        }

        systemFunctionAddresses.forEach { (funcName, funcAddr) ->
            if (funcAddr != null && isInstructionTampered(func_ptr = funcAddr)) {
                add(
                    element = Threat(violation = IosViolation.Hook.InlineHookDetected(name = funcName))
                )
            }
        }
    }
}