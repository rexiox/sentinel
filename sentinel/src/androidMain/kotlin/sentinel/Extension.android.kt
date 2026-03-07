package sentinel

import sentinel.kit.detector.DebugDetector
import sentinel.kit.detector.EmulatorDetector
import sentinel.kit.detector.HookDetector
import sentinel.kit.detector.RootDetector
import sentinel.kit.detector.TamperDetector

fun Builder.all(): Builder = apply {
    root()
    tamper()
    hook()
    emulator()
    debug()
}

fun Builder.root() = apply {
    addDetector(detector = RootDetector(context = context))
}

fun Builder.tamper() = apply {
    addDetector(
        detector = TamperDetector(
            context = context,
            appId = config.appId,
            appSignature = config.signature
        )
    )
}

fun Builder.hook() = apply {
    addDetector(detector = HookDetector())
}

fun Builder.emulator() = apply {
    addDetector(detector = EmulatorDetector())
}

fun Builder.debug() = apply {
    addDetector(detector = DebugDetector(context = context))
}