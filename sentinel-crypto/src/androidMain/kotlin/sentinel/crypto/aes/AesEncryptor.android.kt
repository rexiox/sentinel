package sentinel.crypto.aes

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
actual class AesEncryptor {

    actual fun generateKey(): String = buildString(KEY_LENGTH) {
        repeat(KEY_LENGTH) {
            append(KEY_CHARS[secureRandom.nextInt(KEY_CHARS.length)])
        }
    }

    actual fun generateIv(): ByteArray = ByteArray(IV_LENGTH).also(secureRandom::nextBytes)

    actual fun encrypt(data: String, key: String, iv: ByteArray): String? = runCatching {
        val secretKey = normalizedKey(key) ?: error("Invalid AES key length")
        val cipher = Cipher.getInstance(AES_TRANSFORMATION)

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, IvParameterSpec(iv))
        val encrypted = cipher.doFinal(data.encodeToByteArray())

        Base64.encode(encrypted)
    }.getOrNull()

    actual fun decrypt(data: String, key: String, iv: ByteArray): String? = runCatching {
        val secretKey = normalizedKey(key) ?: error("Invalid AES key length")
        val payload = Base64.decode(data)
        val cipher = Cipher.getInstance(AES_TRANSFORMATION)

        cipher.init(Cipher.DECRYPT_MODE, secretKey, IvParameterSpec(iv))
        cipher.doFinal(payload).decodeToString()
    }.getOrNull()

    private fun normalizedKey(key: String): SecretKeySpec? {
        val keyBytes =
            key.encodeToByteArray().takeIf { it.size in VALID_KEY_LENGTHS } ?: return null
        return SecretKeySpec(keyBytes, AES_ALGORITHM)
    }

    private companion object {

        val secureRandom = SecureRandom()
        val VALID_KEY_LENGTHS = setOf(16, 24, 32)

        const val AES_ALGORITHM = "AES"
        const val AES_TRANSFORMATION = "AES/CBC/PKCS5Padding"
        const val IV_LENGTH = 16
        const val KEY_LENGTH = 32
        const val KEY_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    }
}
