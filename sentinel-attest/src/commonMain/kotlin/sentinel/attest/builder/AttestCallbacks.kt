package sentinel.attest.builder

import sentinel.attest.result.AttestationResult
import sentinel.attest.result.EncryptedAttestation

data class AttestCallbacks(
    val verify: ((EncryptedAttestation) -> Unit),
    val onError: ((AttestationResult.Error) -> Unit),
    val onComplete: (() -> Unit),
)