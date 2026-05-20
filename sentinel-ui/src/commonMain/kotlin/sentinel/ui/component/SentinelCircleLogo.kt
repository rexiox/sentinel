package sentinel.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import sentinel.core.type.RiskLevel
import sentinel.ui.ext.getSentinelRiskLogo
import sentinel.ui.ext.getWaveColor

@Composable
fun SentinelCircleLogo(
    riskLevel: RiskLevel?,
    size: Dp = 76.dp,
    borderWith: Dp = 0.5.dp,
) {
    Image(
        modifier = Modifier
            .size(size = size)
            .clip(shape = CircleShape)
            .border(
                width = borderWith,
                color = riskLevel.getWaveColor().copy(alpha = 0.5f),
                shape = CircleShape
            )
            .alpha(alpha = 0.3f),
        painter = painterResource(resource = riskLevel.getSentinelRiskLogo()),
        contentDescription = "logo"
    )
}