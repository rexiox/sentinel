package sentinel

data class Config(
    var appId: List<Byte> = emptyList(),
    var appIntegrity: List<Byte> = emptyList(),
    var threshold: Int = 90,
    var isLoggingEnabled: Boolean = false,
) {

    internal fun validate() {
        require(appId.isNotEmpty()) {
            "Config validation failed: appId must not be empty"
        }

        require(threshold in 0..500) {
            "Config validation failed: threshold must be between 0 and 500"
        }
    }
}