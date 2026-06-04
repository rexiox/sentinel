package sentinel.crypto.generator

import android.util.Base64
import java.security.SecureRandom

actual fun generateSecureNonce(): String {
    val byteSize = 16
    val nonceBytes = ByteArray(byteSize)
    SecureRandom().nextBytes(nonceBytes)
    return Base64.encodeToString(nonceBytes, Base64.URL_SAFE or Base64.NO_WRAP)
}