package sentinel.crypto.aes

expect class AesEncryptor() {

    fun encrypt(data: String, key: String, iv: ByteArray): String?

    fun decrypt(data: String, key: String, iv: ByteArray): String?

    fun generateKey(): String

    fun generateIv(): ByteArray
}