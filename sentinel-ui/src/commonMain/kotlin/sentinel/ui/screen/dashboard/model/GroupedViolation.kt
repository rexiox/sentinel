package sentinel.ui.screen.dashboard.model

import androidx.compose.runtime.Immutable
import sentinel.core.violation.SecurityViolation
import sentinel.core.detector.Threat
import kotlin.reflect.KClass

@Immutable
data class GroupedViolation(
    val groupName: String,
    val severity: Int,
    val hasDetected: Boolean,
    val threats: List<Threat>,
    val detectedClasses: Set<KClass<out SecurityViolation>>
)