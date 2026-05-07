package sentinel.ui.screen.dashboard.composable

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import sentinel.core.type.RiskLevel

@Composable
internal fun SentinelHeader(
    scrollState: ScrollState,
    appId: String,
    appIntegrity: String,
    riskLevel: RiskLevel,
    severity: Int,
) {
    val collapseRange = 350f

    @Suppress("FrequentlyChangingValue")
    val collapseFraction = (scrollState.value / collapseRange).coerceIn(0f, 1f)

    val scale by animateFloatAsState(
        targetValue = 1f - (collapseFraction * 0.3f),
        label = ""
    )

    val alpha by animateFloatAsState(
        targetValue = 1f - collapseFraction,
        label = ""
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            }
    ) {
        SentinelPackageCard(
            appId = appId,
            appIntegrity = appIntegrity,
            riskLevel = riskLevel
        )

        SentinelRiskLevelCard(
            riskLevel = riskLevel,
            severity = severity
        )

        Spacer(modifier = Modifier.height(height = 16.dp))
    }
}