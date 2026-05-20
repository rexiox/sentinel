package sentinel.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun SentinelChip(
    modifier: Modifier = Modifier,
    text: String = "",
    vector: ImageVector? = null,
    color: Color = Color.White,
    onClick: () -> Unit = {},
) {
    SentinelCard(
        modifier = modifier.clickable(
            indication = ripple(
                bounded = true,
                radius = 4.dp,
                color = color.copy(alpha = 0.1f)
            ),
            interactionSource = null,
            onClick = onClick
        )
    ) {
        if (vector != null) {
            Box(
                modifier = Modifier.padding(all = 6.dp)
            ) {
                Image(
                    modifier = Modifier.size(size = 16.dp),
                    imageVector = vector,
                    contentDescription = "image"
                )
            }
        }

        if (text.isNotBlank()) {
            Text(
                modifier = Modifier.padding(
                    vertical = 4.dp,
                    horizontal = 12.dp
                ),
                text = text,
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                letterSpacing = 1.sp,
                color = color.copy(alpha = 1f)
            )
        }
    }
}