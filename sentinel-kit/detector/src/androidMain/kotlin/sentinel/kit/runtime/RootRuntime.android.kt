@file:OptIn(ExperimentalAtomicApi::class)

package sentinel.kit.runtime

import sentinel.core.violation.AndroidViolation
import sentinel.runtime.Runtime
import kotlin.concurrent.atomics.ExperimentalAtomicApi

object RootRuntime {

    private external fun init(instance: RootRuntime)

    fun onRootDetected() {
        Runtime.onViolationDetected(violation = AndroidViolation.Root.Detected)
    }

    fun initialize() {
        System.loadLibrary("sentinel-root")
        init(this)
    }
}