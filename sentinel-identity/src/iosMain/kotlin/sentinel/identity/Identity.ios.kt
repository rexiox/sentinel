package sentinel.identity

import platform.UIKit.UIDevice
import platform.Foundation.NSBundle
import sentinel.core.identity.Identity
import sentinel.identity.hash.getProvisioningHash

actual class Identity actual constructor(
    context: Any?,
) : Identity {

    actual override val deviceId: String = UIDevice.currentDevice.identifierForVendor?.UUIDString.orEmpty()

    actual override val appId: String = NSBundle.mainBundle.bundleIdentifier.orEmpty()

    actual override val appIntegrity: String? = getProvisioningHash()

    actual override val platform: String = "iOS".lowercase()
}