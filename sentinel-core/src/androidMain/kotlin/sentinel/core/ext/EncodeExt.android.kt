package sentinel.core.ext

import android.util.Base64

actual fun ByteArray.encodeBase64(): String = Base64.encodeToString(this, Base64.NO_WRAP)