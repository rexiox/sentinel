package sentinel.ui.screen.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.rs.sentinel.ui.resources.Res
import com.rs.sentinel.ui.resources.error
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import sentinel.Sentinel
import sentinel.ui.ext.sentinelGradientBackground
import sentinel.ui.screen.dashboard.composable.SentinelDetectors
import sentinel.ui.screen.dashboard.composable.SentinelHeader

@Composable
internal fun SentinelDashboardScreen(
    sentinel: Sentinel,
    appId: String,
    appIntegrity: String,
) {
    val scope = rememberCoroutineScope()

    var state by remember {
        mutableStateOf<SentinelDashboardState>(SentinelDashboardState.Loading)
    }

    val refreshReport = suspend {
        state = runCatching {
            SentinelDashboardState.Success(report = sentinel.inspect())
        }.getOrElse {
            SentinelDashboardState.Error(throwable = it)
        }
    }

    val riskLevel by remember {
        derivedStateOf {
            (state as? SentinelDashboardState.Success)?.report?.riskLevel
        }
    }

    LaunchedEffect(Unit) {
        sentinel.runtime {
            val triggerUpdate = {
                scope.launch {
                    refreshReport()
                }
            }

            onCompromised { triggerUpdate() }
            onTampered { triggerUpdate() }
            onHooked { triggerUpdate() }
            onSimulated { triggerUpdate() }
            onDebugged { triggerUpdate() }
            onCritical { _ -> triggerUpdate() }
            onSafe { triggerUpdate() }
        }
    }

    LaunchedEffect(Unit) {
        refreshReport()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .sentinelGradientBackground(riskLevel = riskLevel)
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
                    appIntegrity = appIntegrity
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

@Composable
private fun SentinelDashboardContent(
    state: SentinelDashboardState.Success,
    appId: String,
    appIntegrity: String,
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
                appIntegrity = appIntegrity,
                riskLevel = state.report.riskLevel,
                severity = state.report.severity
            )

            SentinelDetectors(state = state)
        }
    }
}