package sentinel.ui.screen.scan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import co.rexiox.sentinel.ui.resources.Res
import co.rexiox.sentinel.ui.resources.ic_nav_bar_monitor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.vectorResource
import sentinel.Sentinel
import sentinel.core.report.SecurityReport
import sentinel.ui.component.SentinelChip
import sentinel.ui.component.SentinelCircleLogo
import sentinel.ui.ext.getGradientColors
import sentinel.ui.ext.getWaveColor
import sentinel.ui.screen.scan.composable.info.SentinelInfoPager
import sentinel.ui.screen.scan.composable.SentinelScanEffect
import sentinel.ui.screen.scan.composable.SentinelScanEffectIndicator
import sentinel.ui.screen.scan.model.ScanDetectionResult
import sentinel.ui.screen.scan.model.ScanDetectionType

@Composable
internal fun SentinelScanScreen(
    sentinel: Sentinel,
    onMonitorStart: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    var report by remember { mutableStateOf<SecurityReport?>(null) }
    var showResults by remember { mutableStateOf(false) }
    val scanDetectionResults = remember { mutableStateListOf<ScanDetectionResult>() }

    val refreshReport = suspend {
        report = sentinel.inspect()
    }

    fun addDetectionIfNotExist(result: ScanDetectionResult) {
        if (scanDetectionResults.none { it.type == result.type }) {
            scanDetectionResults.add(result)
        }
    }

    LaunchedEffect(Unit) {
        delay(3000)

        refreshReport()

        sentinel.runtime {
            onCompromised {
                scope.launch { refreshReport() }
                addDetectionIfNotExist(
                    result = ScanDetectionResult(
                        type = if (report?.isRooted == true) {
                            ScanDetectionType.ROOT
                        } else {
                            ScanDetectionType.JAILBREAK
                        },
                        position = Offset(0.3f, 50f),
                        icon = Icons.Default.Warning,
                    )
                )
            }

            onTampered {
                scope.launch { refreshReport() }
                addDetectionIfNotExist(
                    result = ScanDetectionResult(
                        type = ScanDetectionType.TAMPER,
                        position = Offset(0.4f, 145f),
                        icon = Icons.Default.CheckCircle,
                    )
                )
            }

            onHooked {
                scope.launch { refreshReport() }
                addDetectionIfNotExist(
                    result = ScanDetectionResult(
                        type = ScanDetectionType.HOOK,
                        position = Offset(1.75f, 250f),
                        icon = Icons.Default.AddCircle,
                    )
                )
            }

            onSimulated {
                scope.launch { refreshReport() }
                addDetectionIfNotExist(
                    result = ScanDetectionResult(
                        type = if (report?.isEmulator == true) {
                            ScanDetectionType.EMULATOR
                        } else {
                            ScanDetectionType.SIMULATOR
                        },
                        position = Offset(1.4f, 305f),
                        icon = Icons.Default.CheckCircle,
                    )
                )
            }

            onDebugged {
                scope.launch { refreshReport() }
                addDetectionIfNotExist(
                    result = ScanDetectionResult(
                        type = ScanDetectionType.DEBUGGER,
                        position = Offset(3.0f, 275f),
                        icon = Icons.Default.CheckCircle,
                    )
                )
            }

            onSafe {
                scope.launch { refreshReport() }
                scanDetectionResults.clear()
            }

            onCritical { _ ->
                scope.launch { refreshReport() }
            }
        }
    }

    LaunchedEffect(scanDetectionResults.size) {
        if (scanDetectionResults.isNotEmpty()) {
            showResults = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(
                    brush = Brush.radialGradient(
                        colors = report?.riskLevel.getGradientColors(),
                        center = Offset(x = size.width * 0.5f, y = size.height * 0.3f),
                        radius = size.maxDimension * 0.8f
                    ),
                )
            }
    ) {
        SentinelScanEffect(
            modifier = Modifier.fillMaxSize(),
            baseColor = report?.riskLevel.getWaveColor(),
            content = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            vertical = 32.dp,
                            horizontal = 16.dp
                        )
                ) {
                    SentinelChip(
                        modifier = Modifier
                            .align(alignment = Alignment.TopEnd)
                            .padding(vertical = 12.dp),
                        vector = vectorResource(resource = Res.drawable.ic_nav_bar_monitor),
                        onClick = onMonitorStart
                    )

                    Column(
                        modifier = Modifier.align(alignment = Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        SentinelCircleLogo(riskLevel = report?.riskLevel)
                    }

                    SentinelInfoPager(
                        modifier = Modifier
                            .align(alignment = Alignment.BottomCenter)
                            .padding(horizontal = 32.dp)
                            .padding(bottom = 96.dp),
                        report = report
                    )
                }
            }
        )

        SentinelScanEffectIndicator(
            results = scanDetectionResults,
            showResults = showResults
        )
    }
}