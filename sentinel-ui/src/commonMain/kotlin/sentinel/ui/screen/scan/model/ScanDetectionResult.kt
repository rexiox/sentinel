package sentinel.ui.screen.scan.model

import androidx.compose.ui.geometry.Offset

internal data class ScanDetectionResult(
    val type: ScanDetectionType,
    val position: Offset
)