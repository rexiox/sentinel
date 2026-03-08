package sentinel.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import sentinel.core.type.RiskLevel

@Composable
fun SentinelRiskLevelBar(
    modifier: Modifier = Modifier,
    level: RiskLevel,
    severityText: String,
) {
    val levels = RiskLevel.entries

    fun getLevelColor(level: RiskLevel): Color = when (level) {
        RiskLevel.SAFE -> Color(0xFF4CAF50)
        RiskLevel.LOW -> Color(0xFFFFC107)
        RiskLevel.MEDIUM -> Color(0xFFFF9800)
        RiskLevel.HIGH -> Color(0xFFF44336)
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 12.dp)
                    .clip(shape = RoundedCornerShape(percent = 50))
            ) {
                levels.forEach { segment ->
                    val isActive = segment.ordinal <= level.ordinal

                    val color by animateColorAsState(
                        targetValue = getLevelColor(level = segment).copy(alpha = if (isActive) 1f else 0.25f),
                        label = ""
                    )

                    Box(
                        modifier = Modifier
                            .weight(weight = 1f)
                            .fillMaxHeight()
                            .background(color)
                    )
                }
            }

            val indicatorPosition = (level.ordinal + 0.5f) / levels.size

            Box(
                modifier = Modifier
                    .fillMaxWidth(indicatorPosition)
                    .align(Alignment.CenterStart)
            ) {
                Box(
                    modifier = Modifier
                        .align(alignment = Alignment.CenterEnd)
                        .size(size = 8.dp)
                        .clip(shape = CircleShape)
                        .background(color = Color.Black.copy(alpha = 0.75f))
                )
            }
        }

        Spacer(modifier = Modifier.height(height = 6.dp))

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            val indicatorPosition = if (level.ordinal == 0) {
                1f
            } else {
                ((level.ordinal + 0.55f) / levels.size).coerceIn(0f, 1f)
            }

            val textAlign = if (level.ordinal == 0) {
                TextAlign.Start
            } else {
                TextAlign.End
            }

            val animatedSelectedColor by animateColorAsState(
                targetValue = getLevelColor(level = level),
                label = ""
            )

            Text(
                modifier = Modifier
                    .align(alignment = Alignment.CenterStart)
                    .fillMaxWidth(fraction = indicatorPosition),
                text = severityText,
                textAlign = textAlign,
                style = MaterialTheme.typography.labelSmall,
                color = animatedSelectedColor
            )
        }
    }
}