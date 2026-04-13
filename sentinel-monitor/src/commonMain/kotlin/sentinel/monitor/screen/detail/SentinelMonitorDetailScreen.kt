package sentinel.monitor.screen.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rs.sentinel.monitor.resources.Res
import com.rs.sentinel.monitor.resources.analysis_date
import com.rs.sentinel.monitor.resources.detected_threats
import com.rs.sentinel.monitor.resources.no_threats_detected
import com.rs.sentinel.monitor.resources.report_number
import com.rs.sentinel.monitor.resources.risk_status
import com.rs.sentinel.monitor.resources.security_analysis
import com.rs.sentinel.monitor.resources.threat_detail
import com.rs.sentinel.monitor.resources.threat_score
import org.jetbrains.compose.resources.stringResource
import sentinel.core.detector.Threat
import sentinel.core.report.SecurityReport
import sentinel.core.type.RiskLevel
import sentinel.monitor.ext.formatTimestamp
import sentinel.monitor.ext.trimViolationType
import sentinel.ui.ext.getLevelColor

@Composable
internal fun SentinelMonitorDetailContent(report: SecurityReport) {
    val statusColor = report.riskLevel.getLevelColor()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 12.dp,
                bottom = 16.dp
            )
            .padding(horizontal = 16.dp)
            .navigationBarsPadding()
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(resource = Res.string.security_analysis),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f),
            fontWeight = FontWeight.SemiBold
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = statusColor.copy(alpha = 0.1f)
            ),
            border = BorderStroke(
                width = 1.dp,
                color = statusColor.copy(alpha = 0.5f)
            ),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 0.dp
            ),
            shape = RoundedCornerShape(size = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(all = 16.dp)
            ) {
                SentinelMonitorDetailItem(
                    label = stringResource(resource = Res.string.report_number),
                    value = "#${report.hashCode()}",
                )
                SentinelMonitorDetailItem(
                    label = stringResource(resource = Res.string.analysis_date),
                    value = report.timestamp.formatTimestamp()
                )
                SentinelMonitorDetailItem(
                    label = stringResource(resource = Res.string.risk_status),
                    value = report.riskLevel.name,
                    textColor = statusColor
                )
            }
        }

        Text(
            modifier = Modifier.fillMaxWidth()
                .padding(
                    top = 16.dp,
                    bottom = 12.dp
                ),
            text = stringResource(resource = Res.string.detected_threats),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f),
            fontWeight = FontWeight.SemiBold,
        )

        if (report.threats.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(resource = Res.string.no_threats_detected),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f)
                )
            }
        } else {
            report.threats.forEach { threat ->
                SentinelMonitorThreatItem(threat = threat)
            }
        }
    }
}

@Composable
private fun SentinelMonitorThreatItem(threat: Threat) {
    val violation = threat.violation

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(size = 8.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(all = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(weight = 1f)
                ) {
                    Text(
                        text = violation::class.qualifiedName?.trimViolationType().orEmpty(),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.SemiBold
                    )

                    if (!violation.detail.isNullOrBlank()) {
                        Text(
                            text = stringResource(
                                resource = Res.string.threat_detail,
                                violation.detail.orEmpty()
                            ),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f),
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                        )
                    }
                }

                Text(
                    text = stringResource(resource = Res.string.threat_score, violation.severity),
                    style = MaterialTheme.typography.labelSmall,
                    color = RiskLevel.HIGH.getLevelColor()
                )
            }
        }
    }
}

@Composable
private fun SentinelMonitorDetailItem(
    label: String,
    value: String,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor.copy(alpha = 0.75f)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}