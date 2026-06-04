package sentinel.attest.builder

import sentinel.attest.AttestConfig
import sentinel.attest.provider.DefaultNonceProvider
import sentinel.attest.provider.NonceProvider
import sentinel.attest.provider.DefaultAttestProvider
import sentinel.attest.provider.AttestProvider

class AttestBuilder {

    var publicKey: String = ""
    var provider: AttestProvider = DefaultAttestProvider()
    private var nonceProvider: NonceProvider = DefaultNonceProvider()
    private val callbackBuilder = AttestCallbackBuilder()

    fun nonce(provider: NonceProvider) {
        nonceProvider = provider
    }

    fun callbacks(block: AttestCallbackBuilder.() -> Unit) {
        callbackBuilder.block()
    }

    fun build() = AttestConfig(
        rsaPublicKey = publicKey,
        attestProvider = provider,
        nonceProvider = nonceProvider,
        callbacks = callbackBuilder.build()
    )
}