package sentinel.ui.screen.dashboard.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import com.rs.sentinel.ui.resources.Res
import com.rs.sentinel.ui.resources.app_id
import com.rs.sentinel.ui.resources.app_signature
import com.rs.sentinel.ui.resources.ic_sentinel_launcher

@Composable
internal fun SentinelPackageCard(
    modifier: Modifier = Modifier,
    appId: String,
    appSignature: String,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                vertical = 12.dp,
                horizontal = 16.dp
            ),
        shape = RoundedCornerShape(size = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Gray.copy(alpha = 0f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .size(size = 64.dp),
                painter = painterResource(resource = Res.drawable.ic_sentinel_launcher),
                contentDescription = null
            )

            Spacer(modifier = Modifier.height(height = 16.dp))

            InfoLine(
                label = stringResource(resource = Res.string.app_id),
                value = appId
            )

            if (appSignature.isNotBlank()) {
                Spacer(modifier = Modifier.height(height = 12.dp))

                InfoLine(
                    label = stringResource(resource = Res.string.app_signature),
                    value = appSignature
                )
            }
        }
    }
}

@Composable
private fun InfoLine(
    label: String,
    value: String,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        )

        Spacer(modifier = Modifier.height(height = 4.dp))

        Text(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(all = 4.dp),
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            maxLines = if (expanded) Int.MAX_VALUE else 1
        )
    }
}