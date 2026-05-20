package sentinel.ui.screen.scan.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
internal fun SentinelScanEffect(
    modifier: Modifier = Modifier,
    isActive: Boolean = true,
    baseColor: Color = Color(0xFF393939),
    rippleCount: Int = 6,
    maxAlpha: Float = 0.6f,
    animationDuration: Int = 2000,
    content: @Composable (() -> Unit)? = null,
) {
    val alphaStates = remember { List(size = rippleCount) { Animatable(initialValue = 0.1f) } }

    LaunchedEffect(isActive) {
        if (isActive) {
            alphaStates.forEachIndexed { index, anim ->
                launch {
                    delay(index * 300L)

                    while (isActive) {
                        anim.animateTo(
                            targetValue = maxAlpha,
                            animationSpec = tween(
                                durationMillis = animationDuration / 2,
                                easing = LinearEasing
                            )
                        )
                        anim.animateTo(
                            targetValue = 0.1f,
                            animationSpec = tween(
                                durationMillis = animationDuration / 2,
                                easing = LinearEasing
                            )
                        )
                    }
                }
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isActive) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                val center = Offset(x = size.width / 2f, y = size.height / 2f)
                val maxRadius = minOf(size.width, size.height) / 2f * 2f
                val minRadius = maxRadius * 0.2f

                alphaStates.forEachIndexed { index, anim ->
                    val fraction = if (rippleCount > 1) index.toFloat() / (rippleCount - 1) else 0f
                    val radius = minRadius + (maxRadius - minRadius) * fraction
                    val currentAlpha = anim.value

                    drawCircle(
                        color = baseColor.copy(alpha = currentAlpha),
                        radius = radius,
                        center = center,
                        style = Stroke(width = 1.5f)
                    )
                }
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            content?.invoke()
        }
    }
}