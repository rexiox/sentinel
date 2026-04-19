@file:OptIn(ExperimentalAtomicApi::class)

package sentinel.kit.runtime

import sentinel.core.violation.AndroidViolation
import sentinel.runtime.Runtime
import kotlin.concurrent.atomics.ExperimentalAtomicApi

object DebugRuntime {

    private external fun init(instance: DebugRuntime)

    fun initialize() {
        loadLibrary()
        init(this)
    }

    private fun loadLibrary() {
        System.loadLibrary("sentinel-debugger")
    }

    fun onDebuggerDetected() {
        Runtime.onViolationDetected(violation = AndroidViolation.Debugger.Detected)
    }
}