package sentinel.identity

import android.content.Context
import sentinel.core.identity.Identity
import sentinel.identity.ext.getAndroidId
import sentinel.identity.ext.getAppSignatureSHA256

actual class Identity private constructor(
    context: Context,
) : Identity {

    actual override val deviceId: String = context.getAndroidId()

    actual override val appId: String = context.packageName

    actual override val appIntegrity: String? = context.getAppSignatureSHA256()

    actual override val platform: String = "Android"

    actual companion object {

        private val instances = lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            mutableMapOf<String, Identity>()
        }

        actual fun getInstance(context: Any?): Identity {
            require(context is Context) { "Android requires Context" }

            val appContext = context.applicationContext
            val packageName = appContext.packageName

            return synchronized(instances.value) {
                instances.value.getOrPut(packageName) {
                    Identity(appContext)
                }
            }
        }
    }
}