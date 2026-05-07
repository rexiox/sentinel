package sentinel.ui.screen.dashboard.composable

import androidx.compose.ui.graphics.Color

data class SentinelCardColors(
    val borderGradient: List<Color>,
    val backgroundGradient: List<Color>,
    val iconColor: Color,
    val shadowColor: Color,
    val textColor: Color,
)

val SafeCardColors = SentinelCardColors(
    borderGradient = listOf(
        Color(0xFF5B5B5B),
        Color(0xFF656565),
        Color(0xFF5B5B5B)
    ),
    backgroundGradient = listOf(
        Color(0xFF5B5B5B).copy(alpha = 0.55f),
        Color(0xFF5B5B5B).copy(alpha = 0.50f)
    ),
    iconColor = Color(0xFFFFFFFF),
    shadowColor = Color(0xFF5B5B5B),
    textColor = Color(0xFFFFFFFF)
)

val DangerCardColors = SentinelCardColors(
    borderGradient = listOf(
        Color(0xFFEC1C24),
        Color(0xFFA80606),
        Color(0xFFEC1C24)
    ),
    backgroundGradient = listOf(
        Color(0xFFFF2830).copy(alpha = 0.65f),
        Color(0xFFFF2830).copy(alpha = 0.50f)
    ),
    iconColor = Color(0xFF6D0004),
    shadowColor = Color(0xFFFF1744),
    textColor = Color(0xFFFFFFFF)
)