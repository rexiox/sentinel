package sentinel

import android.content.Context
import sentinel.core.detector.SecurityDetector
import sentinel.identity.Identity

actual class Builder {

    internal lateinit var context: Context
    private val detectors = mutableListOf<SecurityDetector>()
    internal val config = Config()

    internal fun setContext(context: Context): Builder = apply {
        this.context = context
    }

    actual fun addDetector(detector: SecurityDetector) = apply {
        detectors.add(element = detector)
    }

    actual fun config(block: Config.() -> Unit): Config = config.apply(block = block)

    actual fun build(): Sentinel = Sentinel(detectors = detectors, config = config)
}

fun Sentinel.Companion.configure(
    context: Context,
    block: Builder.() -> Unit,
): Sentinel {
    Sentinel.Identity = lazy { Identity(context = context) }.value

    return Builder()
        .setContext(context = context)
        .apply(block = block)
        .build()
}