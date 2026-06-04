package sentinel.attest.provider

class DefaultAttestProvider : AttestProvider {

    override suspend fun getToken(nonce: String): String = ""
}