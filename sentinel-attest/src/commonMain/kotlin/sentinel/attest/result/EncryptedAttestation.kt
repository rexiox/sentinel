package sentinel.attest.result

import kotlinx.serialization.Serializable

@Serializable
data class EncryptedAttestation(
    val encryptedKey: String,
    val encryptedData: String,
    val iv: String,
)