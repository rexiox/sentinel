package sentinel.monitor

import android.content.Context
import android.content.Intent

/**
 * SentinelMonitor serves as the primary entry point for managing application integrity
 * and security monitoring processes.
 *
 * This object provides the interface required to initiate the [SentinelMonitorActivity]
 * for verifying application security.
 */
object SentinelMonitor {

    /**
     * Initiates the security monitoring process.
     *
     * Starts a monitoring session using the provided application ID and integrity data.
     * The operation is launched in a new task using [Intent.FLAG_ACTIVITY_NEW_TASK].
     *
     * @param context The [Context] used to start the activity.
     * @param appId A list of bytes representing the unique application identifier.
     * @param appIntegrity The package signature used to verify the application.
     * @param threshold The threshold value defined for security checks.
     */
    fun start(
        context: Context,
        appId: List<Byte>,
        appIntegrity: List<Byte>,
        threshold: Int,
    ) {
        val intent = SentinelMonitorActivity.newIntent(
            context = context,
            appId = appId.toByteArray(),
            appIntegrity = appIntegrity.toByteArray(),
            threshold = threshold
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        context.startActivity(intent)
    }
}