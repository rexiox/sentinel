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
                    baseColor.copy(alpha = 0.7f),
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
                    baseColor.copy(alpha = 0.4f),
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

fun RiskLevel?.getGradientColors(): List<Color> = when (this) {
    RiskLevel.SAFE -> listOf(
        Color(0xFF0F9015),
        Color(0xFF0A6110),
        Color(0xFF0D2B0F)
    )

    RiskLevel.LOW -> listOf(
        Color(0xFFA57C02),
        Color(0xFF68500A),
        Color(0xFF2B2000)
    )

    RiskLevel.MEDIUM -> listOf(
        Color(0xFF935C0B),
        Color(0xFF864F01),
        Color(0xFF331E00)
    )

    RiskLevel.HIGH -> listOf(
        Color(0xFF8C1014),
        Color(0xFF6A0D12),
        Color(0xFF7D0A0E)
    )

    else -> {
        listOf(
            Color(0xFF131313),
            Color(0xFF111111),
            Color(0xFF000000)
        )
    }
}