package sentinel.ui.screen.dashboard.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rs.sentinel.ui.resources.Res
import com.rs.sentinel.ui.resources.detectors
import org.jetbrains.compose.resources.stringResource
import sentinel.core.detector.Threat
import sentinel.ui.screen.dashboard.SentinelDashboardState
import sentinel.ui.screen.dashboard.model.GroupedViolation
import sentinel.ui.violation.getGroupName
import sentinel.ui.violation.getViolations

@Composable
internal fun SentinelDetectors(
    state: SentinelDashboardState.Success,
) {
    val groupedViolations = remember(state.report) {
        val detected = state.report.threats.map { threat -> threat.violation::class }.toSet()
        val allViolations = getViolations()

        allViolations.groupBy(::getGroupName).map { (groupName, violations) ->
            val hasDetected = violations.any { violation -> detected.contains(violation::class) }
            val groupSeverity = violations.sumOf { violation ->
                if (detected.contains(violation::class)) violation.severity else 0
            }

            GroupedViolation(
                groupName = groupName,
                severity = groupSeverity,
                hasDetected = hasDetected,
                threats = violations.map { Threat(violation = it) },
                detectedClasses = detected
            )
        }
    }

    Column(
        modifier = Modifier.padding(bottom = 96.dp)
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp,
                    bottom = 8.dp
                ),
            text = stringResource(resource = Res.string.detectors),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        groupedViolations.forEach { groupedViolation ->
            SentinelDetectCard(
                detectorName = groupedViolation.groupName,
                detectorSeverity = groupedViolation.severity.toString(),
                threats = groupedViolation.threats,
                detected = groupedViolation.detectedClasses,
                colors = if (groupedViolation.hasDetected) DangerCardColors else SafeCardColors
            )
        }
    }
}