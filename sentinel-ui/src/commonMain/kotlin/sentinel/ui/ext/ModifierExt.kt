package sentinel.ui.ext

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import sentinel.core.type.RiskLevel

fun Modifier.sentinelGradientBackground(riskLevel: RiskLevel?): Modifier {
    if (riskLevel == null) return Modifier

    val baseColor = riskLevel.getLevelColor()

    return drawBehind {
        drawRect(color = Color(0xFF0A0000))

        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    baseColor.copy(alpha = 0.4f),
                    Color.Transparent
                ),
                center = Offset(
                    x = size.width * 0.8f,
                    y = size.height * 0.2f
                ),
                radius = size.width * 1.2f
            )
        )

        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    baseColor.copy(alpha = 0.3f),
                    Color.Transparent
                ),
                center = Offset(
                    x = size.width * 0.4f,
                    y = size.height * 0.5f
                ),
                radius = size.width * 0.8f
            )
        )
    }
}