package sentinel.monitor.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import sentinel.core.report.SecurityReport
import sentinel.monitor.ext.formatTimestamp
import sentinel.ui.ext.getLevelColor

@Composable
internal fun SentinelMonitorCard(report: SecurityReport, onClick: () -> Unit) {
    val statusColor = report.riskLevel.getLevelColor()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(size = 12.dp)
                    .background(
                        color = statusColor,
                        shape = CircleShape
                    )
            )

            Spacer(modifier = Modifier.width(width = 16.dp))

            Column(
                modifier = Modifier.weight(weight = 1f)
            ) {
                Text(
                    text = "#${report.hashCode()}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                )

                Spacer(modifier = Modifier.height(height = 6.dp))

                Text(
                    text = report.timestamp.formatTimestamp(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f),
                )
            }

            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = report.riskLevel.name,
                color = statusColor,
                style = MaterialTheme.typography.labelSmall,
            )
        }

        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f)
        )
    }
}