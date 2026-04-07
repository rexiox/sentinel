package sentinel.monitor

import android.content.Context
import android.content.Intent
import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.setResourceReaderAndroidContext
import sentinel.Sentinel
import sentinel.all
import sentinel.configure

class SentinelMonitorActivity : ComponentActivity() {

    @ExperimentalResourceApi
    override fun onCreate(savedInstanceState: Bundle?) {

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                lightScrim = TRANSPARENT,
                darkScrim = TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                scrim = TRANSPARENT,
                darkScrim = TRANSPARENT
            )
        )

        val appIdBytes = intent.getByteArrayExtra(EXTRA_APP_ID)?.toList() ?: emptyList()
        val signatureBytes = intent.getByteArrayExtra(EXTRA_SIGNATURE)?.toList() ?: emptyList()
        val threshold = intent.getIntExtra(EXTRA_THRESHOLD, 100)

        super.onCreate(savedInstanceState)
        setResourceReaderAndroidContext(this)

        setContent {
            val context = LocalContext.current

            val sentinel = remember {
                Sentinel.configure(context = context) {
                    config {
                        this.appId = appIdBytes
                        this.signature = signatureBytes
                        this.threshold = threshold
                    }

                    all()
                }
            }

            App(sentinel = sentinel)
        }
    }

    companion object {

        private const val EXTRA_APP_ID = "EXTRA_APP_ID"
        private const val EXTRA_SIGNATURE = "EXTRA_SIGNATURE"
        private const val EXTRA_THRESHOLD = "EXTRA_THRESHOLD"

        fun newIntent(
            context: Context,
            appId: ByteArray,
            signature: ByteArray,
            threshold: Int,
        ): Intent = Intent(context, SentinelMonitorActivity::class.java).apply {
            putExtra(EXTRA_APP_ID, appId)
            putExtra(EXTRA_SIGNATURE, signature)
            putExtra(EXTRA_THRESHOLD, threshold)
        }
    }
}