package sentinel.crypto.rsa

expect class RsaEncryptor(publicKey: String) {

    fun encryptKey(plainKey: String): String?
}