package sentinel.ui.screen.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rs.sentinel.ui.resources.Res
import com.rs.sentinel.ui.resources.about_github_label
import com.rs.sentinel.ui.resources.about_github_url
import com.rs.sentinel.ui.resources.about_github_url_text
import com.rs.sentinel.ui.resources.about_license_label
import com.rs.sentinel.ui.resources.about_license_url
import com.rs.sentinel.ui.resources.about_license_url_text
import com.rs.sentinel.ui.resources.app_description
import com.rs.sentinel.ui.resources.app_mame
import com.rs.sentinel.ui.resources.ic_sentinel_launcher
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import sentinel.ui.component.SentinelLinkButton
import sentinel.ui.screen.dashboard.composable.SentinelInfoItem

@Composable
internal fun SentinelAboutScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .verticalScroll(state = scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(height = 60.dp))

        Image(
            modifier = Modifier.size(size = 48.dp),
            painter = painterResource(resource = Res.drawable.ic_sentinel_launcher),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(height = 32.dp))

        SentinelInfoItem(
            modifier = Modifier.padding(horizontal = 16.dp),
            title = stringResource(resource = Res.string.app_mame),
            subtitle = stringResource(resource = Res.string.app_description),
            isExpanded = true
        )

        Spacer(modifier = Modifier.height(height = 32.dp))

        SentinelLinkButton(
            title = stringResource(resource = Res.string.about_github_label),
            linkText = stringResource(resource = Res.string.about_github_url_text),
            url = stringResource(resource = Res.string.about_github_url),
        )

        Spacer(modifier = Modifier.height(height = 32.dp))

        SentinelLinkButton(
            title = stringResource(resource = Res.string.about_license_label),
            linkText = stringResource(resource = Res.string.about_license_url_text),
            url = stringResource(resource = Res.string.about_license_url),
        )

        Spacer(modifier = Modifier.height(height = 96.dp))
    }
}