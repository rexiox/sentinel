package sentinel.ui.ext

import androidx.compose.ui.graphics.Color
import sentinel.core.type.RiskLevel

fun RiskLevel.getLevelColor(): Color = when (this) {
    RiskLevel.SAFE -> Color(0xFF4CAF50)
    RiskLevel.LOW -> Color(0xFFFFC107)
    RiskLevel.MEDIUM -> Color(0xFFFF9800)
    RiskLevel.HIGH -> Color(0xFFEA1B22)
}

internal fun RiskLevel?.getWaveColor(): Color = when (this) {
    RiskLevel.SAFE -> Color(0x654CAF50)
    RiskLevel.LOW -> Color(0x51FFC107)
    RiskLevel.MEDIUM -> Color(0x56FF9800)
    RiskLevel.HIGH -> Color(0x85FF313A)
    null -> Color(0xFF313131)
}

internal fun RiskLevel?.getGradientColors(): List<Color> = when (this) {
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
        Color(0xFFCD0007),
        Color(0xFF790006),
        Color(0xFF490002)
    )

    else -> listOf(
        Color(0xFF0A0A0A),
        Color(0xFF0A0A0A),
        Color(0xFF000000)
    )
}