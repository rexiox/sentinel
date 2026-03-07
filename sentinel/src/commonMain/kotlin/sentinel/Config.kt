package sentinel

data class Config(
    var appId: List<Byte>? = null,
    var signature: List<Byte>? = null,
    var threshold: Int = 90,
    var isLoggingEnabled: Boolean = false
)