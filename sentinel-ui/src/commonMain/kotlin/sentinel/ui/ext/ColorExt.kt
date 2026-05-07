package sentinel.ui.ext

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import sentinel.core.type.RiskLevel

fun RiskLevel.getLevelColor(): Color = when (this) {
    RiskLevel.SAFE -> Color(0xFF4CAF50)
    RiskLevel.LOW -> Color(0xFFFFC107)
    RiskLevel.MEDIUM -> Color(0xFFFF9800)
    RiskLevel.HIGH -> Color(0xFFEA1B22)
}

fun RiskLevel.getLogoColorFilter(): ColorFilter? = when (this) {
    RiskLevel.SAFE -> ColorFilter.colorMatrix(
        ColorMatrix(
            values = floatArrayOf(
                0f, 0.7f, 0f, 0f, 0f,
                0f, 1f, 1f, 0f, 1f,
                0f, 0.3f, 0.5f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    )

    RiskLevel.LOW -> ColorFilter.colorMatrix(
        ColorMatrix(
            values = floatArrayOf(
                1f, 0.8f, 0f, 0f, 0f,
                0.8f, 0.9f, 0f, 0f, 0f,
                0f, 0f, 0.2f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    )

    RiskLevel.MEDIUM -> ColorFilter.colorMatrix(
        ColorMatrix(
            values = floatArrayOf(
                1f, 0.6f, 0f, 0f, 0f,
                0.6f, 0.8f, 0f, 0f, 0f,
                0f, 0f, 0.2f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
    )

    RiskLevel.HIGH -> null
}