package sentinel.ui.ext

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import sentinel.core.type.RiskLevel

fun Modifier.sentinelGradientBackground(
    riskLevel: RiskLevel? = null,
): Modifier {
    val baseColor = when (riskLevel) {
        RiskLevel.SAFE -> Color(0xFF1B5E20)
        RiskLevel.LOW -> Color(0xFFFBC02D)
        RiskLevel.MEDIUM -> Color(0xFFF57C00)
        RiskLevel.HIGH -> Color(0xFFD32F2F)
        else -> Color(0xFF424242)
    }

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