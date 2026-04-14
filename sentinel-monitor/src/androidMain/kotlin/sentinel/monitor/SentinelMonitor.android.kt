package sentinel.monitor

import android.content.Context
import android.content.Intent

object SentinelMonitor {

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