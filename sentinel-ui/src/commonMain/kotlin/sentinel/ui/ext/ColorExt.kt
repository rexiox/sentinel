package sentinel.ui.ext

import androidx.compose.ui.graphics.Color
import sentinel.core.type.RiskLevel

fun RiskLevel.getLevelColor(): Color = when (this) {
    RiskLevel.SAFE -> Color(0xFF4CAF50)
    RiskLevel.LOW -> Color(0xFFFFC107)
    RiskLevel.MEDIUM -> Color(0xFFFF9800)
    RiskLevel.HIGH -> Color(0xFFF44336)
}