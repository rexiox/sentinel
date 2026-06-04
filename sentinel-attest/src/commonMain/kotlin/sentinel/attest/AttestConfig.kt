package sentinel.attest

import sentinel.attest.builder.AttestCallbacks
import sentinel.attest.provider.AttestProvider
import sentinel.attest.provider.NonceProvider

data class AttestConfig(
    val rsaPublicKey: String,
    val attestProvider: AttestProvider,
    val nonceProvider: NonceProvider,
    val callbacks: AttestCallbacks
)