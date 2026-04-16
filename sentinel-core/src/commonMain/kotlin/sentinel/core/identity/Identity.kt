package sentinel.core.identity

interface Identity {
    val deviceId: String
    val appId: String
    val appIntegrity: String?
    val platform: String
}