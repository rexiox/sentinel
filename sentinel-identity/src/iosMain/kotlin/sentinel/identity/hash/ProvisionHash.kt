package sentinel.identity.hash

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.usePinned
import platform.CoreCrypto.CC_SHA256
import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.dataWithContentsOfFile
import platform.Foundation.stringWithFormat

@OptIn(ExperimentalForeignApi::class)
internal fun getProvisioningHash(): String? {
    val bundle = NSBundle.mainBundle
    val path = bundle.pathForResource("embedded", "mobileprovision") ?: return null
    val data = NSData.dataWithContentsOfFile(path) ?: return null
    val bytes = data.bytes?.reinterpret<ByteVar>() ?: return null
    val length = data.length.toUInt()

    val digest = ByteArray(32)
    digest.usePinned { pinned ->
        CC_SHA256(bytes, length, pinned.addressOf(0).reinterpret())
    }

    return digest.toHexString()
}

private fun ByteArray.toHexString(): String = joinToString("") { byte ->
    NSString.stringWithFormat("%02x", byte.toInt() and 0xFF)
}