package sentinel.ui.screen.scan.composable.info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import co.rexiox.sentinel.ui.resources.Res
import co.rexiox.sentinel.ui.resources.detected_text
import co.rexiox.sentinel.ui.resources.info_debugger_title
import co.rexiox.sentinel.ui.resources.info_device_integrity_title
import co.rexiox.sentinel.ui.resources.info_hook_title
import co.rexiox.sentinel.ui.resources.info_tamper_title
import co.rexiox.sentinel.ui.resources.info_emulator_and_simulator_title
import co.rexiox.sentinel.ui.resources.risk_level
import co.rexiox.sentinel.ui.resources.safe_text
import co.rexiox.sentinel.ui.resources.severity
import co.rexiox.sentinel.ui.resources.threshold
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import sentinel.core.report.SecurityReport
import sentinel.ui.component.SentinelCard
import sentinel.ui.component.SentinelChip
import kotlin.math.absoluteValue

@Composable
internal fun SentinelInfoPager(
    modifier: Modifier = Modifier,
    report: SecurityReport?,
    autoScrollDelayMillis: Long = 3000L,
) {
    if (report == null) return

    val pages = remember(report) { buildReportPageList(report) }
    val pagerState = rememberPagerState(pageCount = pages::size)

    LaunchedEffect(key1 = pages) {
        while (true) {
            delay(timeMillis = autoScrollDelayMillis)

            if (!pagerState.isScrollInProgress) {
                val nextPage = (pagerState.currentPage + 1) % pages.size
                pagerState.animateScrollToPage(page = nextPage)
            }
        }
    }

    Column(
        modifier = modifier
    ) {
        SentinelCard(
            shape = RoundedCornerShape(size = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalPager(
                    modifier = Modifier.fillMaxWidth(),
                    state = pagerState,
                ) { pageId ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(height = 28.dp)
                            .graphicsLayer {
                                val offset =
                                    ((pagerState.currentPage - pageId) + pagerState.currentPageOffsetFraction).absoluteValue
                                alpha = 1f - (offset * 0.9f)
                                val scale = 1f - (offset * 0.15f)
                                scaleX = scale
                                scaleY = scale
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        when (val page = pages[pageId]) {
                            is SentinelInfoPage.Overview -> SentinelInfoOverviewPageContent(page)
                            is SentinelInfoPage.Detail -> SentinelInfoDetailPageContent(page)
                            is SentinelInfoPage.Status -> SentinelInfoStatusPageContent(page)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(height = 8.dp))

        SentinelInfoPagerIndicator(
            pagerState = pagerState,
            pages = pages
        )
    }
}

@Composable
private fun SentinelInfoPagerIndicator(
    pagerState: PagerState,
    pages: List<SentinelInfoPage>,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pages.size) { iteration ->
            val isSelected = pagerState.currentPage == iteration
            val width = if (isSelected) 16.dp else 6.dp

            SentinelCard(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .clip(shape = CircleShape)
                    .size(
                        width = width,
                        height = 6.dp
                    )
            )
        }
    }
}

private fun buildReportPageList(report: SecurityReport): List<SentinelInfoPage> = buildList {
    add(
        SentinelInfoPage.Overview(
            severity = report.severity,
            threshold = report.threshold
        )
    )
    add(
        SentinelInfoPage.Detail(
            titleResource = Res.string.risk_level,
            text = report.riskLevel.name
        )
    )
    add(
        SentinelInfoPage.Status(
            titleResource = Res.string.info_device_integrity_title,
            status = report.isCompromised
        )
    )
    add(
        SentinelInfoPage.Status(
            titleResource = Res.string.info_tamper_title,
            status = report.isTampered
        )
    )
    add(
        SentinelInfoPage.Status(
            titleResource = Res.string.info_hook_title,
            status = report.isHooked
        )
    )
    add(
        SentinelInfoPage.Status(
            titleResource = Res.string.info_emulator_and_simulator_title,
            status = report.isEmulator || report.isSimulator
        )
    )
    add(
        SentinelInfoPage.Status(
            titleResource = Res.string.info_debugger_title,
            status = report.isDebugged
        )
    )
}

@Composable
private fun SentinelInfoOverviewPageContent(page: SentinelInfoPage.Overview) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${stringResource(resource = Res.string.threshold)}: ",
            style = MaterialTheme.typography.bodyMedium
        )

        SentinelChip(text = page.threshold.toString())

        Spacer(modifier = Modifier.width(width = 12.dp))

        Text(
            text = "${stringResource(resource = Res.string.severity)}: ",
            style = MaterialTheme.typography.bodyMedium
        )

        SentinelChip(text = page.severity.toString())
    }
}

@Composable
private fun SentinelInfoDetailPageContent(page: SentinelInfoPage.Detail) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${stringResource(resource = page.titleResource)}: ",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.width(width = 4.dp))

        SentinelChip(text = page.text)
    }
}

@Composable
private fun SentinelInfoStatusPageContent(page: SentinelInfoPage.Status) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val statusText = if (page.status) Res.string.detected_text else Res.string.safe_text

        Text(
            text = "${stringResource(resource = page.titleResource)}: ",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.width(width = 4.dp))

        SentinelChip(text = stringResource(resource = statusText))
    }
}