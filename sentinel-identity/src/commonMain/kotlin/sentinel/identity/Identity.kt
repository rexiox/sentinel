package sentinel.identity

import sentinel.core.identity.Identity

expect class Identity : Identity {

    override val deviceId: String

    override val appId: String

    override val appIntegrity: String?

    override val platform: String

    companion object {

        fun getInstance(context: Any? = null): Identity
    }
}