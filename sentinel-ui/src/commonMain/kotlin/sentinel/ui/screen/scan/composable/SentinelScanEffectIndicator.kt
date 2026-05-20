package sentinel.ui.screen.scan.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sentinel.ui.component.SentinelCard
import sentinel.ui.component.SentinelChip
import sentinel.ui.screen.scan.model.ScanDetectionResult
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
internal fun SentinelScanEffectIndicator(
    modifier: Modifier = Modifier,
    results: List<ScanDetectionResult>,
    showResults: Boolean = false,
    rippleCount: Int = 6,
) {
    if (!showResults) return

    val density = LocalDensity.current
    val canvasSize = remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { canvasSize.value = it }
    ) {
        val width = canvasSize.value.width.toFloat()
        val height = canvasSize.value.height.toFloat()

        if (width > 0 && height > 0) {
            val centerX = width / 2f
            val centerY = height / 2f

            val maxRadius = minOf(width, height) / 2f * 2f
            val minRadius = maxRadius * 0.2f

            results.forEach { result ->
                key(result.type) {
                    val scale = remember { Animatable(0f) }
                    val alphaAnim = remember { Animatable(0f) }

                    LaunchedEffect(showResults) {
                        delay(timeMillis = (100..300).random().toLong())

                        launch {
                            scale.animateTo(
                                targetValue = 1f,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                        }
                        launch {
                            alphaAnim.animateTo(
                                targetValue = 1f,
                                animationSpec = tween(durationMillis = 300)
                            )
                        }
                    }

                    val ringIndex = result.position.x.coerceIn(0f, (rippleCount - 1).toFloat())
                    val fraction = if (rippleCount > 1) ringIndex / (rippleCount - 1) else 0f
                    val targetRadius = minRadius + (maxRadius - minRadius) * fraction

                    val angleInRadians = (result.position.y * (PI / 180.0)).toFloat()

                    val indicatorX = centerX + targetRadius * cos(angleInRadians)
                    val indicatorY = centerY + targetRadius * sin(angleInRadians)

                    val offsetX = with(density) { indicatorX.toDp() }
                    val offsetY = with(density) { indicatorY.toDp() }

                    Box(
                        modifier = Modifier
                            .offset(x = offsetX, y = offsetY)
                            .graphicsLayer {
                                scaleX = scale.value
                                scaleY = scale.value
                                alpha = alphaAnim.value
                                translationX = -size.width / 2f
                                translationY = -size.height / 2f
                            }
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            SentinelCard(
                                modifier = Modifier
                                    .size(size = 8.dp)
                                    .background(
                                        color = Color.Red.copy(alpha = 0.3f),
                                        shape = CircleShape
                                    )
                            )

                            Spacer(modifier = Modifier.width(width = 8.dp))

                            SentinelChip(text = result.type.name)
                        }
                    }
                }
            }
        }
    }
}