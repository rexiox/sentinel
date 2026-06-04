package sentinel.crypto.rsa

import java.security.KeyFactory
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
actual class RsaEncryptor actual constructor(
    private val publicKey: String,
) {

    actual fun encryptKey(plainKey: String): String? = runCatching {
        val sanitizedKey = publicKey
            .lineSequence()
            .map(String::trim)
            .filter(String::isNotEmpty)
            .filterNot { it.startsWith("-----BEGIN") || it.startsWith("-----END") }
            .joinToString(separator = "")
            .takeIf(String::isNotEmpty)
            ?: return null

        val publicKeyBytes = Base64.decode(sanitizedKey)
        val keySpec = X509EncodedKeySpec(publicKeyBytes)
        val publicKey = KeyFactory.getInstance(RSA_ALGORITHM).generatePublic(keySpec)
        val cipher = Cipher.getInstance(RSA_TRANSFORMATION)

        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
        val encrypted = cipher.doFinal(plainKey.encodeToByteArray())

        Base64.encode(encrypted)
    }.onFailure {
        it.printStackTrace()
    }.getOrNull()

    private companion object {

        const val RSA_ALGORITHM = "RSA"
        const val RSA_TRANSFORMATION = "RSA/ECB/PKCS1Padding"
    }
}
