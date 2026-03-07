package sentinel

import sentinel.core.detector.SecurityDetector
import sentinel.identity.Identity

actual class Builder {

    private val detectors = mutableListOf<SecurityDetector>()
    internal val config = Config()

    actual fun config(block: Config.() -> Unit): Config = config.apply(block = block)

    actual fun addDetector(detector: SecurityDetector) = apply {
        detectors.add(element = detector)
    }

    actual fun build(): Sentinel = Sentinel(detectors = detectors, config = config)
}

fun Sentinel.Companion.configure(
    block: Builder.() -> Unit,
): Sentinel {
    Sentinel.Identity = lazy { Identity() }.value

    return Builder()
        .apply(block = block)
        .build()
}