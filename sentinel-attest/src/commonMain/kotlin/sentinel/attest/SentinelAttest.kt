package sentinel.attest

import sentinel.attest.builder.AttestBuilder
import sentinel.attest.crypto.PayloadEncryptor
import sentinel.attest.result.AttestationPayload
import sentinel.attest.result.AttestationResult
import sentinel.attest.result.toDto
import sentinel.attest.exception.AttestationErrorCode
import sentinel.core.ext.currentTimeMillis
import sentinel.core.platform.getPlatform
import sentinel.core.report.SecurityReport

class SentinelAttest private constructor(
    private val config: AttestConfig,
    private val encryptor: PayloadEncryptor,
) {

    companion object {

        fun configure(
            block: AttestBuilder.() -> Unit,
        ): SentinelAttest {
            val config = AttestBuilder().apply(block).build()

            return SentinelAttest(
                config = config,
                encryptor = PayloadEncryptor(rsaPublicKey = config.rsaPublicKey)
            )
        }
    }

    suspend fun execute(report: SecurityReport) = runCatching {
        val nonce = config.nonceProvider.get()
        val attestationToken = config.attestProvider.getToken(nonce = nonce)

        val payload = AttestationPayload(
            nonce = nonce,
            attestationToken = attestationToken,
            securityReport = report.toDto(),
            platform = getPlatform().id,
            timestamp = currentTimeMillis(),
        )

        val encrypted = encryptor.encrypt(attestationPayload = payload)

        config.callbacks.verify(encrypted)
    }.onFailure { throwable ->
        config.callbacks.onError(
            AttestationResult.Error(
                cause = throwable,
                code = AttestationErrorCode.EXECUTE
            )
        )
    }.run {
        config.callbacks.onComplete()
    }
}