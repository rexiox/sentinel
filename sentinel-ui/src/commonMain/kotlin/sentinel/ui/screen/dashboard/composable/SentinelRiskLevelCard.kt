package sentinel.ui.screen.dashboard.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rs.sentinel.ui.resources.Res
import com.rs.sentinel.ui.resources.risk_high
import com.rs.sentinel.ui.resources.risk_level
import com.rs.sentinel.ui.resources.risk_low
import com.rs.sentinel.ui.resources.risk_medium
import com.rs.sentinel.ui.resources.risk_safe
import com.rs.sentinel.ui.resources.severity
import org.jetbrains.compose.resources.stringResource
import sentinel.core.type.RiskLevel
import sentinel.ui.component.SentinelRiskLevelBar

@Composable
fun SentinelRiskLevelCard(riskLevel: RiskLevel, severity: Int) {
    val riskDescription = when (riskLevel) {
        RiskLevel.SAFE -> stringResource(resource = Res.string.risk_safe)
        RiskLevel.LOW -> stringResource(resource = Res.string.risk_low)
        RiskLevel.MEDIUM -> stringResource(resource = Res.string.risk_medium)
        RiskLevel.HIGH -> stringResource(resource = Res.string.risk_high)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(size = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            SentinelInfoItem(
                title = stringResource(resource = Res.string.risk_level),
                subtitle = riskDescription,
                isExpanded = true
            )

            Spacer(modifier = Modifier.height(height = 16.dp))

            SentinelRiskLevelBar(
                level = riskLevel,
                severityText = "${stringResource(resource = Res.string.severity)}: $severity"
            )
        }
    }
}