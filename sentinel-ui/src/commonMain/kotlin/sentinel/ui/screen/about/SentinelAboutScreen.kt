package sentinel.ui.screen.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import co.rexiox.sentinel.ui.resources.Res
import co.rexiox.sentinel.ui.resources.about_github_label
import co.rexiox.sentinel.ui.resources.about_github_url
import co.rexiox.sentinel.ui.resources.about_github_url_text
import co.rexiox.sentinel.ui.resources.about_license_label
import co.rexiox.sentinel.ui.resources.about_license_url
import co.rexiox.sentinel.ui.resources.about_license_url_text
import co.rexiox.sentinel.ui.resources.app_description
import co.rexiox.sentinel.ui.resources.app_mame
import org.jetbrains.compose.resources.stringResource
import sentinel.core.type.RiskLevel
import sentinel.ui.component.SentinelLinkButton
import sentinel.ui.screen.dashboard.composable.SentinelInfoItem
import sentinel.ui.screen.dashboard.composable.SentinelLogo

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

        SentinelLogo(riskLevel = RiskLevel.HIGH)

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