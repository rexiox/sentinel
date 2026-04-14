package sentinel.core.ext

actual fun String?.toByteList(): List<Byte> = this?.encodeToByteArray()?.toList().orEmpty()