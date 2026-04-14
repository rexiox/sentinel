package sentinel.ui.screen.dashboard.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rs.sentinel.ui.resources.Res
import com.rs.sentinel.ui.resources.app_id
import com.rs.sentinel.ui.resources.app_signature
import com.rs.sentinel.ui.resources.ic_sentinel_launcher
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun SentinelPackageCard(
    modifier: Modifier = Modifier,
    appId: String,
    appIntegrity: String
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp),
        shape = RoundedCornerShape(size = 8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
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

            SentinelInfoItem(
                title = stringResource(resource = Res.string.app_id),
                subtitle = appId
            )

            if (appIntegrity.isNotBlank()) {
                Spacer(modifier = Modifier.height(height = 12.dp))

                SentinelInfoItem(
                    title = stringResource(resource = Res.string.app_signature),
                    subtitle = appIntegrity
                )
            }
        }
    }
}