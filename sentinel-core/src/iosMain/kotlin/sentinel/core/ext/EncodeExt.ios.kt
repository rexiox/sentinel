package sentinel.core.ext

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.base64EncodedStringWithOptions
import platform.Foundation.dataWithBytes

@OptIn(ExperimentalForeignApi::class)
actual fun ByteArray.encodeBase64(): String {
    val nsData = this.usePinned { pinned ->
        NSData.dataWithBytes(pinned.addressOf(0), this.size.toULong())
    }
    return nsData.base64EncodedStringWithOptions(0u)
}