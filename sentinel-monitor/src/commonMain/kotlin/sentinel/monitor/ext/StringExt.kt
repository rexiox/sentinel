package sentinel.monitor.ext

internal fun String?.trimViolationType(): String = this?.trim()
    ?.removePrefix("//")
    ?.split('.')
    ?.takeLast(2)
    ?.joinToString(".")
    .orEmpty()