package sentinel.monitor.screen.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import sentinel.Sentinel
import sentinel.core.report.SecurityReport
import sentinel.monitor.component.SentinelMonitorCard
import sentinel.monitor.component.SentinelMonitorTopBar
import sentinel.monitor.screen.detail.SentinelMonitorDetailContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SentinelMonitorScreen(
    modifier: Modifier = Modifier,
    sentinel: Sentinel,
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    var reports by remember { mutableStateOf(listOf<SecurityReport>()) }
    var selectedReport by remember { mutableStateOf<SecurityReport?>(null) }

    val refreshReport = suspend {
        runCatching {
            val newReport = sentinel.inspect()
            reports = (listOf(newReport) + reports).distinctBy { it.timestamp }
        }
    }

    LaunchedEffect(Unit) {
        refreshReport()

        sentinel.runtime {
            val updateAction = {
                scope.launch { refreshReport() }
                Unit
            }

            onCompromised(updateAction)
            onTampered(updateAction)
            onHooked(updateAction)
            onSimulated(updateAction)
            onDebugged(updateAction)
            onSafe(updateAction)
            onCritical { _ -> updateAction() }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            SentinelMonitorTopBar(
                threshold = sentinel.config.threshold,
                onRefresh = {
                    scope.launch {
                        refreshReport()
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = padding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding()
                )
        ) {
            items(reports) { report ->
                SentinelMonitorCard(
                    report = report,
                    onClick = {
                        selectedReport = report
                        showBottomSheet = true
                    }
                )
            }
        }
    }

    if (showBottomSheet) {
        selectedReport?.let { report ->
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = Color(0xFF151515),
                shape = RoundedCornerShape(size = 8.dp),
                dragHandle = {
                    BottomSheetDefaults.DragHandle(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f)
                    )
                }
            ) {
                SentinelMonitorDetailContent(report = report)
            }
        }
    }
}