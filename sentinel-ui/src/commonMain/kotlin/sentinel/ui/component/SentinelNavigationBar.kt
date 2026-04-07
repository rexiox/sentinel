package sentinel.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rs.sentinel.ui.resources.Res
import com.rs.sentinel.ui.resources.ic_nav_bar_about
import com.rs.sentinel.ui.resources.ic_nav_bar_home
import com.rs.sentinel.ui.resources.ic_nav_bar_monitor
import com.rs.sentinel.ui.resources.nav_bar_about_item
import com.rs.sentinel.ui.resources.nav_bar_dashboard_item
import com.rs.sentinel.ui.resources.nav_bar_monitor_item
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import sentinel.ui.screen.main.tab.SentinelTab

@Composable
internal fun SentinelNavigationBar(
    modifier: Modifier = Modifier,
    selectedTab: SentinelTab,
    onTabSelected: (SentinelTab) -> Unit,
) {
    val tabs = SentinelTab.entries

    var selectedTabItem by remember { mutableStateOf(0f) }
    var selectedTabWidth by remember { mutableStateOf(0f) }

    val animX by animateFloatAsState(
        targetValue = selectedTabItem,
        animationSpec = SentinelSpringSpec
    )
    val animWidth by animateFloatAsState(
        targetValue = selectedTabWidth,
        animationSpec = SentinelSpringSpec
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .height(height = 60.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(
                    radius = 24.dp,
                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                )
                .background(
                    color = Color.Black.copy(alpha = 0.9f),
                    shape = CircleShape
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = CircleShape)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.08f),
                            Color.White.copy(alpha = 0.03f)
                        )
                    ),
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.3f),
                            Color.White.copy(alpha = 0.03f),
                            Color.White.copy(alpha = 0.1f)
                        )
                    ),
                    shape = CircleShape
                )
                .drawWithContent {
                    val paddingPx = 6.dp.toPx()

                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.DarkGray.copy(alpha = 0.20f),
                                Color.DarkGray.copy(alpha = 0.05f),
                                Color.Transparent
                            )
                        ),
                        topLeft = Offset(
                            x = animX + paddingPx,
                            y = paddingPx
                        ),
                        size = Size(
                            width = animWidth - (paddingPx * 2),
                            height = size.height - (paddingPx * 2)
                        ),
                        cornerRadius = CornerRadius(
                            x = 100f,
                            y = 100f
                        )
                    )

                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.09f),
                                Color.Transparent,
                                Color.Transparent
                            ),
                        ),
                        topLeft = Offset(
                            x = animX + paddingPx,
                            y = paddingPx
                        ),
                        size = Size(
                            width = animWidth - (paddingPx * 2),
                            height = size.height - (paddingPx * 2)
                        ),
                        cornerRadius = CornerRadius(
                            x = 100f,
                            y = 100f
                        ),
                        style = Stroke(width = 1.2.dp.toPx())
                    )

                    drawContent()
                }
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEach { tab ->
                    val isSelected = selectedTab == tab

                    SentinelNavigationBarItem(
                        modifier = Modifier
                            .weight(weight = 1f)
                            .onGloballyPositioned {
                                if (isSelected) {
                                    @Suppress("AssignedValueIsNeverRead")
                                    selectedTabItem = it.positionInParent().x
                                    @Suppress("AssignedValueIsNeverRead")
                                    selectedTabWidth = it.size.width.toFloat()
                                }
                            },
                        selected = isSelected,
                        icon = when (tab) {
                            SentinelTab.Dashboard -> {
                                vectorResource(resource = Res.drawable.ic_nav_bar_home)
                            }

                            SentinelTab.Monitor -> {
                                vectorResource(resource = Res.drawable.ic_nav_bar_monitor)
                            }

                            else -> {
                                vectorResource(resource = Res.drawable.ic_nav_bar_about)
                            }
                        },
                        label = when (tab) {
                            SentinelTab.Dashboard -> stringResource(resource = Res.string.nav_bar_dashboard_item)
                            SentinelTab.Monitor -> stringResource(resource = Res.string.nav_bar_monitor_item)
                            SentinelTab.About -> stringResource(resource = Res.string.nav_bar_about_item)
                        },
                        onClick = {
                            onTabSelected(tab)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SentinelNavigationBarItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    val contentColor by animateColorAsState(
        targetValue = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            Color.White.copy(alpha = 0.5f)
        },
        animationSpec = tween(durationMillis = 400)
    )

    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            modifier = Modifier
                .size(size = 22.dp)
                .scale(scale = if (selected) 1.1f else 1f),
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
        )

        Spacer(modifier = Modifier.height(height = 2.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = contentColor,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

private val SentinelSpringSpec = spring<Float>(
    dampingRatio = 0.6f,
    stiffness = Spring.StiffnessLow
)