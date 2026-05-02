package sentinel.identity

import platform.Foundation.NSBundle
import platform.UIKit.UIDevice
import sentinel.core.identity.Identity
import sentinel.identity.hash.getProvisioningHash

actual class Identity : Identity {

    actual override val deviceId: String = UIDevice.currentDevice.identifierForVendor?.UUIDString.orEmpty()

    actual override val appId: String = NSBundle.mainBundle.bundleIdentifier.orEmpty()

    actual override val appIntegrity: String? = getProvisioningHash()

    actual override val platform: String = "iOS".lowercase()

    actual companion object {

        private val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            Identity()
        }

        actual fun getInstance(context: Any?): Identity = instance
    }
}