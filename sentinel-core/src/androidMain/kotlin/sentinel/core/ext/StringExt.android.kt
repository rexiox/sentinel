package sentinel.core.ext

actual fun String?.toByteList(): List<Byte> = when {
    this != null -> toByteArray(Charsets.UTF_8).toList()
    else -> emptyList()
}