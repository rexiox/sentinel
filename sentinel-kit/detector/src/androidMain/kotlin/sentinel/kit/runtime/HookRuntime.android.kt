@file:OptIn(ExperimentalAtomicApi::class)

package sentinel.kit.runtime

import sentinel.core.violation.AndroidViolation
import sentinel.runtime.Runtime
import kotlin.concurrent.atomics.ExperimentalAtomicApi

object HookRuntime {

    private external fun init(instance: HookRuntime)

    fun initialize() {
        loadLibrary()
        init(this)
    }

    private fun loadLibrary() {
        System.loadLibrary("sentinel-hook")
    }

    fun onHookDetected() {
        Runtime.onViolationDetected(violation = AndroidViolation.Hook.Detected)
    }
}