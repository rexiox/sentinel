package sentinel.identity

import android.content.Context
import sentinel.core.identity.Identity
import sentinel.identity.ext.getAndroidId
import sentinel.identity.ext.getAppSignatureSHA256

actual class Identity actual constructor(
    private val context: Any?,
) : Identity {

    private val androidContext: Context
        get() = context as? Context
            ?: throw IllegalArgumentException("Identity requires a valid Android Context")

    actual override val deviceId: String = androidContext.getAndroidId()

    actual override val appId: String = androidContext.packageName

    actual override val appIntegrity: String? = androidContext.getAppSignatureSHA256()

    actual override val platform: String = "Android"
}