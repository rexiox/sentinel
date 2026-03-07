package sentinel

import sentinel.kit.detector.DebugDetector
import sentinel.kit.detector.SimulatorDetector
import sentinel.kit.detector.HookDetector
import sentinel.kit.detector.JailbreakDetector
import sentinel.kit.detector.TamperDetector

fun Builder.all(): Builder = apply {
    jailbreak()
    tamper()
    hook()
    simulator()
    debug()
}

fun Builder.jailbreak() = apply {
    addDetector(detector = JailbreakDetector())
}

fun Builder.tamper() = apply {
    addDetector(
        detector = TamperDetector(
            appId = config.appId,
            appSignature = config.signature
        )
    )
}

fun Builder.hook() = apply {
    addDetector(detector = HookDetector())
}

fun Builder.simulator() = apply {
    addDetector(detector = SimulatorDetector())
}

fun Builder.debug() = apply {
    addDetector(detector = DebugDetector())
}