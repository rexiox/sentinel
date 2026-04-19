@file:OptIn(ExperimentalForeignApi::class)

package sentinel.kit.detector

import kotlinx.cinterop.ExperimentalForeignApi
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.IosViolation
import sentinel.detector.isDebuggerAttached

open class DebugDetector : SecurityDetector {

    override fun detect(): List<Threat> = buildList {
        if (isDebuggerAttached()) {
            add(element = Threat(violation = IosViolation.Debugger.DebuggerAttached))
        }
    }
}