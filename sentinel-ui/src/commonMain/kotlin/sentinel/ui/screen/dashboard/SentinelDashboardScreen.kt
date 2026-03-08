package sentinel.ui.screen.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rs.sentinel.ui.resources.Res
import com.rs.sentinel.ui.resources.error
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import sentinel.Sentinel
import sentinel.ui.ext.sentinelGradientBackground
import sentinel.ui.screen.dashboard.composable.SentinelDetectors
import sentinel.ui.screen.dashboard.composable.SentinelHeader

@Composable
internal fun SentinelDashboardScreen(
    sentinel: Sentinel,
    appId: String,
    appSignature: String,
) {
    val state by produceState<SentinelDashboardState>(initialValue = SentinelDashboardState.Loading) {
        value = withContext(context = Dispatchers.IO) {
            runCatching {
                SentinelDashboardState.Success(report = sentinel.inspect())
            }.getOrElse {
                SentinelDashboardState.Error(throwable = it)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .sentinelGradientBackground(
                riskLevel = (state as? SentinelDashboardState.Success)?.report?.riskLevel
            )
    ) {
        when (val state = state) {
            is SentinelDashboardState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            is SentinelDashboardState.Success -> {
                SentinelDashboardContent(
                    state = state,
                    appId = appId,
                    appSignature = appSignature
                )
            }

            is SentinelDashboardState.Error -> {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "${stringResource(resource = Res.string.error)}: ${state.throwable.message}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SentinelDashboardContent(
    state: SentinelDashboardState.Success,
    appId: String,
    appSignature: String,
) {
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
    ) { _ ->
        Column(
            modifier = Modifier.verticalScroll(state = scrollState)
        ) {
            SentinelHeader(
                scrollState = scrollState,
                appId = appId,
                appSignature = appSignature,
                riskLevel = state.report.riskLevel,
                severity = state.report.severity
            )

            SentinelDetectors(state = state)
        }
    }
}