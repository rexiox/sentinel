package sentinel.monitor

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import sentinel.Sentinel
import sentinel.all
import sentinel.configure

fun SentinelMonitorController(
    appId: List<Byte>,
    appIntegrity: List<Byte>,
    threshold: Int
) = ComposeUIViewController {

    val sentinel = remember {
        Sentinel.configure {
            config {
                this.appId = appId
                this.appIntegrity = appIntegrity
                this.threshold = threshold
            }

            all()
        }
    }

    App(sentinel = sentinel)
}