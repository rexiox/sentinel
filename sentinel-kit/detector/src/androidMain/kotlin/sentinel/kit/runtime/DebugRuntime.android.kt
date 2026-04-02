@file:OptIn(ExperimentalAtomicApi::class)

package sentinel.kit.runtime

import sentinel.core.violation.AndroidViolation
import sentinel.runtime.Runtime
import kotlin.concurrent.atomics.ExperimentalAtomicApi

object DebugRuntime {

    private external fun init(instance: DebugRuntime)

    fun onDebuggerDetected() {
        Runtime.onViolationDetected(violation = AndroidViolation.Debugger.Detected)
    }

    fun initialize() {
        System.loadLibrary("sentinel-debugger")
        init(this)
    }
}