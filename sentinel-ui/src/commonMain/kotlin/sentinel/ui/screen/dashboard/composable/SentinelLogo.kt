package sentinel.ui.screen.dashboard.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import sentinel.core.type.RiskLevel
import sentinel.ui.ext.getSentinelRiskLogo

@Composable
internal fun SentinelLogo(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    riskLevel: RiskLevel? = null,
    shape: Shape = RoundedCornerShape(size = 14.dp),
) {
    Image(
        modifier = modifier
            .size(size = size)
            .shadow(
                elevation = 3.dp,
                shape = shape,
                clip = false
            )
            .clip(shape = shape),
        painter = painterResource(resource = riskLevel.getSentinelRiskLogo()),
        contentScale = ContentScale.Crop,
        contentDescription = "logo"
    )
}