package sentinel.attest.provider

interface AttestProvider {

    suspend fun getToken(nonce: String): String
}