package sentinel.ui.screen.dashboard.composable

import androidx.compose.ui.graphics.Color

data class SentinelCardColors(
    val backgroundGradient: List<Color>,
    val iconColor: Color,
    val textColor: Color,
)

val SafeCardColors = SentinelCardColors(
    backgroundGradient = listOf(
        Color(0xFF5B5B5B).copy(alpha = 0.55f),
        Color(0xFF5B5B5B).copy(alpha = 0.50f)
    ),
    iconColor = Color(0xFFFFFFFF),
    textColor = Color(0xFFFFFFFF)
)

val DangerCardColors = SentinelCardColors(
    backgroundGradient = listOf(
        Color(0xFFD2060D).copy(alpha = 0.85f),
        Color(0xFFC1080E).copy(alpha = 0.8f)
    ),
    iconColor = Color(0xFF6F0006),
    textColor = Color(0xFFFFFFFF)
)