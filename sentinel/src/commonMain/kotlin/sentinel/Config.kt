package sentinel

import sentinel.core.logger.SentinelLogger

data class Config(
    var appId: List<Byte> = emptyList(),
    var appIntegrity: List<Byte> = emptyList(),
    var threshold: Int = 90,
    var isLoggingEnabled: Boolean = false,
) {

    internal fun validate() = runCatching {
        require(appId.isNotEmpty()) {
            "Config validation failed: appId must not be empty"
        }

        require(appIntegrity.isNotEmpty()) {
            "Config validation failed: appIntegrity must not be empty"
        }

        require(threshold in 0..500) {
            "Config validation failed: threshold must be between 0 and 500"
        }
    }.onFailure { exception ->
        SentinelLogger.print(msg = exception.message.orEmpty())
    }
}