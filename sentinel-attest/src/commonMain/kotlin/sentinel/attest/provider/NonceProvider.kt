package sentinel.attest.provider

fun interface NonceProvider {

    suspend fun get(): String
}