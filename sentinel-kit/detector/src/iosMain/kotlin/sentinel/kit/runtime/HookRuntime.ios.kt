@file:OptIn(ExperimentalForeignApi::class)

package sentinel.kit.runtime

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.staticCFunction
import sentinel.core.violation.IosViolation
import sentinel.detector.setViolationHandler
import sentinel.runtime.Runtime

private fun onHookDetected() {
    Runtime.onViolationDetected(violation = IosViolation.Hook.Detected)
}

object HookRuntime {

    fun initialize() {
        setViolationHandler(handler = staticCFunction(::onHookDetected))
    }
}