package sentinel.ui.screen.scan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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

    fun addDetectionIfNotExist(type: ScanDetectionType, position: Offset) {
        if (scanDetectionResults.none { it.type == type }) {
            scanDetectionResults.add(
                ScanDetectionResult(
                    type = type,
                    position = position
                )
            )
        }
    }

    fun updateDetectionsFromReport(report: SecurityReport?) {
        report ?: return

        scanDetectionResults.clear()

        if (report.isCompromised) {
            addDetectionIfNotExist(
                type = if (report.isRooted) {
                    ScanDetectionType.ROOT
                } else {
                    ScanDetectionType.JAILBREAK
                },
                position = Offset(0.3f, 50f)
            )
        }

        if (report.isTampered) {
            addDetectionIfNotExist(
                type = ScanDetectionType.TAMPER,
                position = Offset(0.4f, 145f)
            )
        }

        if (report.isHooked) {
            addDetectionIfNotExist(
                type = ScanDetectionType.HOOK,
                position = Offset(1.75f, 250f)
            )
        }

        if (report.isEmulator || report.isSimulator) {
            addDetectionIfNotExist(
                type = if (report.isEmulator) {
                    ScanDetectionType.EMULATOR
                } else {
                    ScanDetectionType.SIMULATOR
                },
                position = Offset(1.4f, 305f)
            )
        }

        if (report.isDebugged) {
            addDetectionIfNotExist(
                type = ScanDetectionType.DEBUGGER,
                position = Offset(3.0f, 275f)
            )
        }
    }

    val refreshReport: suspend () -> Unit = {
        report = sentinel.inspect()
        updateDetectionsFromReport(report = report)
    }

    LaunchedEffect(Unit) {
        delay(timeMillis = 3000)

        refreshReport()

        sentinel.runtime {
            onCompromised {
                scope.launch { refreshReport() }
            }

            onTampered {
                scope.launch { refreshReport() }
            }

            onHooked {
                scope.launch { refreshReport() }
            }

            onSimulated {
                scope.launch { refreshReport() }
            }

            onDebugged {
                scope.launch { refreshReport() }
            }

            onSafe {
                scope.launch { refreshReport() }
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
                        center = Offset(
                            x = size.width * 0.5f,
                            y = size.height * 0.3f
                        ),
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
                            .padding(vertical = 18.dp),
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
            riskLevel = report?.riskLevel,
            showResults = showResults
        )
    }
}