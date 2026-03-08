package sentinel.ui.screen.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import sentinel.Sentinel
import sentinel.ui.component.SentinelNavigationBar
import sentinel.ui.screen.about.SentinelAboutScreen
import sentinel.ui.screen.dashboard.SentinelDashboardScreen
import sentinel.ui.screen.main.tab.SentinelTab

@Composable
fun SentinelMainScreen(
    modifier: Modifier = Modifier,
    navigationBarModifier: Modifier = Modifier,
    sentinel: Sentinel,
    appId: String,
    appSignature: String,
) {
    var selectedTab by remember { mutableStateOf(SentinelTab.Dashboard) }
    val onTabSelected = remember { { tab: SentinelTab -> selectedTab = tab } }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            SentinelNavigationBar(
                modifier = navigationBarModifier,
                selectedTab = selectedTab,
                onTabSelected = onTabSelected
            )
        },
    ) { _ ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            when (selectedTab) {
                SentinelTab.Dashboard -> {
                    SentinelDashboardScreen(
                        sentinel = sentinel,
                        appId = appId,
                        appSignature = appSignature
                    )
                }

                SentinelTab.About -> {
                    SentinelAboutScreen()
                }
            }
        }
    }
}