package sentinel.attest.builder

import sentinel.attest.result.AttestationResult
import sentinel.attest.result.EncryptedAttestation

class AttestCallbackBuilder {

    private var verifyBlock: ((EncryptedAttestation) -> Unit) = {}
    private var onErrorBlock: ((AttestationResult.Error) -> Unit) = {}
    private var onCompleteBlock: (() -> Unit) = {}

    fun verify(block: (EncryptedAttestation) -> Unit) {
        this.verifyBlock = block
    }

    fun onError(block: (AttestationResult.Error) -> Unit) {
        this.onErrorBlock = block
    }

    fun onComplete(block: () -> Unit) {
        this.onCompleteBlock = block
    }

    fun build() = AttestCallbacks(
        verify = verifyBlock,
        onError = onErrorBlock,
        onComplete = onCompleteBlock
    )
}