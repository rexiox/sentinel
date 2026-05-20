package sentinel.ui.screen.scan.composable.info

import org.jetbrains.compose.resources.StringResource

internal sealed interface SentinelInfoPage {

    data class Overview(val severity: Int, val threshold: Int) : SentinelInfoPage

    data class Detail(val titleResource: StringResource, val text: String) : SentinelInfoPage

    data class Status(val titleResource: StringResource, val status: Boolean) : SentinelInfoPage
}