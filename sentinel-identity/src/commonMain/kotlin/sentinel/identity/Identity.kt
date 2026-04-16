package sentinel.identity

import sentinel.core.identity.Identity

expect class Identity(context: Any? = null) : Identity {

    override val deviceId: String

    override val appId: String

    override val appIntegrity: String?

    override val platform: String
}