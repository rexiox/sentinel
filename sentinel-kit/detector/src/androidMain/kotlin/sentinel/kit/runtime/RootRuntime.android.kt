@file:OptIn(ExperimentalAtomicApi::class)

package sentinel.kit.runtime

import sentinel.core.handler.ExceptionHandler
import sentinel.core.violation.AndroidViolation
import sentinel.runtime.Runtime
import kotlin.concurrent.atomics.ExperimentalAtomicApi

object RootRuntime {

    private external fun init(instance: RootRuntime)

    fun initialize() {
        ExceptionHandler.safely(context = "RootRuntime.initialize") {
            loadLibrary()
            init(this)
        }
    }

    private fun loadLibrary() {
        System.loadLibrary("sentinel-root")
    }

    fun onRootDetected() {
        Runtime.onViolationDetected(violation = AndroidViolation.Root.Detected)
    }
}