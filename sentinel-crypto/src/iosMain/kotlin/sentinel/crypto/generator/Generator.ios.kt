package sentinel.crypto.generator

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSData
import platform.Foundation.base64EncodedStringWithOptions
import platform.Foundation.create
import platform.Security.SecRandomCopyBytes
import platform.Security.kSecRandomDefault
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual fun generateSecureNonce(): String {
    val byteSize = 16
    val nonceBytes = ByteArray(byteSize)

    nonceBytes.usePinned { pinned ->
        SecRandomCopyBytes(kSecRandomDefault, byteSize.toULong(), pinned.addressOf(0))
    }

    val nsData =
        NSData.create(bytes = nonceBytes.usePinned { it.addressOf(0) }, length = byteSize.toULong())
    val base64String = nsData.base64EncodedStringWithOptions(0UL)

    return base64String
        .replace("+", "-")
        .replace("/", "_")
        .replace("=", "")
}