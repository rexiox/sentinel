package sentinel.crypto.rsa

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.autoreleasepool
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.usePinned
import platform.CoreFoundation.CFDataRef
import platform.CoreFoundation.CFDictionaryCreateMutable
import platform.CoreFoundation.CFDictionarySetValue
import platform.CoreFoundation.CFErrorRefVar
import platform.CoreFoundation.CFRelease
import platform.CoreFoundation.CFTypeRef
import platform.CoreFoundation.kCFAllocatorDefault
import platform.Foundation.CFBridgingRelease
import platform.Foundation.CFBridgingRetain
import platform.Foundation.NSData
import platform.Foundation.NSNumber
import platform.Foundation.create
import platform.Security.SecKeyCreateEncryptedData
import platform.Security.SecKeyCreateWithData
import platform.Security.SecKeyIsAlgorithmSupported
import platform.Security.SecKeyRef
import platform.Security.kSecAttrKeyClass
import platform.Security.kSecAttrKeyClassPublic
import platform.Security.kSecAttrKeySizeInBits
import platform.Security.kSecAttrKeyType
import platform.Security.kSecAttrKeyTypeRSA
import platform.Security.kSecKeyAlgorithmRSAEncryptionPKCS1
import platform.Security.kSecKeyOperationTypeEncrypt
import platform.posix.memcpy
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class, ExperimentalEncodingApi::class)
actual class RsaEncryptor actual constructor(
    private val publicKey: String,
) {

    actual fun encryptKey(plainKey: String): String? = runCatching {
        val publicKey = createPublicKey() ?: return null

        try {
            check(
                SecKeyIsAlgorithmSupported(
                    publicKey,
                    kSecKeyOperationTypeEncrypt,
                    kSecKeyAlgorithmRSAEncryptionPKCS1
                )
            ) { "Unsupported RSA algorithm" }

            memScoped {
                val plainKeyBytes = plainKey.encodeToByteArray()
                val nsData = if (plainKeyBytes.isEmpty()) {
                    NSData.create(bytes = null, length = 0u)
                } else {
                    plainKeyBytes.usePinned { pinned ->
                        NSData.create(
                            bytes = pinned.addressOf(0),
                            length = plainKeyBytes.size.toULong()
                        )
                    }
                }

                @Suppress("UNCHECKED_CAST")
                val cfData = CFBridgingRetain(nsData) as CFDataRef

                try {
                    val errorRef = alloc<CFErrorRefVar>()
                    val encrypted = SecKeyCreateEncryptedData(
                        publicKey,
                        kSecKeyAlgorithmRSAEncryptionPKCS1,
                        cfData,
                        errorRef.ptr
                    ) ?: error("RSA encryption failed")

                    Base64.encode((CFBridgingRelease(encrypted) as NSData).toByteArray())
                } finally {
                    CFRelease(cfData)
                }
            }
        } finally {
            CFRelease(publicKey)
        }
    }.getOrNull()

    private fun createPublicKey(): SecKeyRef? = autoreleasepool {
        memScoped {
            val sanitizedKey = publicKey
                .lineSequence()
                .map(String::trim)
                .filter(String::isNotEmpty)
                .filterNot { it.startsWith("-----BEGIN") || it.startsWith("-----END") }
                .joinToString(separator = "")
                .takeIf(String::isNotEmpty)
                ?: return@memScoped null

            val publicKeyBytes = Base64.decode(sanitizedKey)
            val publicKeyData = if (publicKeyBytes.isEmpty()) {
                return@memScoped null
            } else {
                publicKeyBytes.usePinned { pinned ->
                    NSData.create(
                        bytes = pinned.addressOf(0),
                        length = publicKeyBytes.size.toULong()
                    )
                }
            }

            val attributes = CFDictionaryCreateMutable(kCFAllocatorDefault, 0, null, null).apply {
                CFDictionarySetValue(this, kSecAttrKeyType, kSecAttrKeyTypeRSA)
                CFDictionarySetValue(this, kSecAttrKeyClass, kSecAttrKeyClassPublic)
                CFDictionarySetValue(
                    this,
                    kSecAttrKeySizeInBits,
                    CFBridgingRetain(NSNumber(int = 2048))
                )
            }

            val errorRef = alloc<CFErrorRefVar>()

            @Suppress("UNCHECKED_CAST")
            val cfData = CFBridgingRetain(publicKeyData) as CFDataRef

            try {
                SecKeyCreateWithData(cfData, attributes, errorRef.ptr)
            } finally {
                CFRelease(cfData)
                CFRelease(attributes as CFTypeRef?)
            }
        }
    }

    private fun NSData.toByteArray(): ByteArray = ByteArray(length.toInt()).also { bytes ->
        bytes.usePinned { pinned ->
            memcpy(pinned.addressOf(0), this.bytes, length)
        }
    }
}
