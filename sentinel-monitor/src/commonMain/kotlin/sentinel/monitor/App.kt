package sentinel.monitor

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import sentinel.Sentinel
import sentinel.monitor.screen.main.SentinelMonitorScreen
import sentinel.ui.theme.SentinelTheme

@Composable
internal fun App(sentinel: Sentinel) {
    SentinelTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Black,
        ) {
            SentinelMonitorScreen(
                sentinel = sentinel,
            )
        }
    }
}