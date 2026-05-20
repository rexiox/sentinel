package sentinel.ui.screen.scan.model

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.vector.ImageVector

internal data class ScanDetectionResult(
    val type: ScanDetectionType,
    val position: Offset,
    val icon: ImageVector,
)