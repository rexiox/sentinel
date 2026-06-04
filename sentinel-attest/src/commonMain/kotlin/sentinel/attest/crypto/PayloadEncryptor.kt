package sentinel.attest.crypto

import kotlinx.serialization.json.Json
import sentinel.attest.result.AttestationPayload
import sentinel.attest.result.EncryptedAttestation
import sentinel.core.ext.encodeBase64
import sentinel.crypto.aes.AesEncryptor
import sentinel.crypto.rsa.RsaEncryptor

class PayloadEncryptor(rsaPublicKey: String) {

    private val rsaEncryptor by lazy { RsaEncryptor(publicKey = rsaPublicKey) }
    private val aesEncryptor by lazy { AesEncryptor() }

    fun encrypt(attestationPayload: AttestationPayload): EncryptedAttestation {
        val attestationPayloadJson = Json.encodeToString(attestationPayload)
        val iv = aesEncryptor.generateIv()
        val aesKey = aesEncryptor.generateKey()
        val encryptedKey = rsaEncryptor.encryptKey(plainKey = aesKey)
        val encryptedData = aesEncryptor.encrypt(
            data = attestationPayloadJson,
            key = aesKey,
            iv = iv
        )

        return EncryptedAttestation(
            encryptedKey = encryptedKey.orEmpty(),
            encryptedData = encryptedData.orEmpty(),
            iv = iv.encodeBase64()
        )
    }
}