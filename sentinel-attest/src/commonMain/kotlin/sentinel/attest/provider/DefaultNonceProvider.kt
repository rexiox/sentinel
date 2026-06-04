package sentinel.attest.provider

class DefaultNonceProvider : NonceProvider {

    override suspend fun get(): String = throw NotImplementedError()
}