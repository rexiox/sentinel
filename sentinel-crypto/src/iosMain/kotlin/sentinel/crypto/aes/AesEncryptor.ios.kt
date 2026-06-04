package sentinel.crypto.aes

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ULongVar
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import platform.CoreCrypto.CCCrypt
import platform.CoreCrypto.kCCAlgorithmAES128
import platform.CoreCrypto.kCCBlockSizeAES128
import platform.CoreCrypto.kCCDecrypt
import platform.CoreCrypto.kCCEncrypt
import platform.CoreCrypto.kCCOptionPKCS7Padding
import platform.CoreCrypto.kCCSuccess
import platform.Security.SecRandomCopyBytes
import platform.Security.errSecSuccess
import platform.Security.kSecRandomDefault
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalForeignApi::class, ExperimentalEncodingApi::class)
actual class AesEncryptor {

    actual fun generateKey(): String = buildString(KEY_LENGTH) {
        repeat(KEY_LENGTH) {
            append(KEY_CHARS[randomIndex(KEY_CHARS.length)])
        }
    }

    actual fun generateIv(): ByteArray = randomBytes(size = IV_LENGTH)

    actual fun encrypt(data: String, key: String, iv: ByteArray): String? = runCatching {
        val keyBytes = normalizedKey(key) ?: error("Invalid AES key length")
        val encrypted = crypt(
            operation = kCCEncrypt,
            data = data.encodeToByteArray(),
            key = keyBytes,
            iv = iv
        )

        Base64.encode(encrypted)
    }.getOrNull()

    actual fun decrypt(data: String, key: String, iv: ByteArray): String? = runCatching {
        val keyBytes = normalizedKey(key) ?: error("Invalid AES key length")
        val decoded = Base64.decode(data)
        require(decoded.size > IV_LENGTH) { "Invalid AES payload" }

        val payload = decoded.copyOfRange(IV_LENGTH, decoded.size)
        val decrypted = crypt(
            operation = kCCDecrypt,
            data = payload,
            key = keyBytes,
            iv = iv
        )

        decrypted.decodeToString()
    }.getOrNull()

    private fun normalizedKey(key: String): ByteArray? =
        key.encodeToByteArray().takeIf { it.size in VALID_KEY_LENGTHS }

    private fun crypt(
        operation: UInt,
        data: ByteArray,
        key: ByteArray,
        iv: ByteArray,
    ): ByteArray = memScoped {
        val output = ByteArray(data.size + kCCBlockSizeAES128.toInt())
        val outputLength = alloc<ULongVar>()

        val status = key.usePinned { keyPinned ->
            iv.usePinned { ivPinned ->
                data.usePinned { dataPinned ->
                    output.usePinned { outputPinned ->
                        CCCrypt(
                            operation,
                            kCCAlgorithmAES128,
                            kCCOptionPKCS7Padding,
                            keyPinned.addressOf(0),
                            key.size.toULong(),
                            ivPinned.addressOf(0),
                            dataPinned.addressOf(0),
                            data.size.toULong(),
                            outputPinned.addressOf(0),
                            output.size.toULong(),
                            outputLength.ptr
                        )
                    }
                }
            }
        }

        check(status == kCCSuccess) { "AES operation failed with status: $status" }
        output.copyOf(outputLength.value.toInt())
    }

    private fun randomIndex(bound: Int): Int = randomBytes(4)
        .fold(0) { acc, byte -> (acc shl 8) or (byte.toInt() and 0xFF) }
        .let { it and Int.MAX_VALUE }
        .mod(bound)

    private fun randomBytes(size: Int): ByteArray = ByteArray(size).also { bytes ->
        val status = bytes.usePinned { pinned ->
            SecRandomCopyBytes(kSecRandomDefault, size.toULong(), pinned.addressOf(0))
        }
        check(status == errSecSuccess) { "Unable to generate secure random bytes" }
    }

    private companion object {

        val VALID_KEY_LENGTHS = setOf(16, 24, 32)

        const val IV_LENGTH = 16
        const val KEY_LENGTH = 32
        const val KEY_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    }
}
