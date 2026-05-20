package sentinel.ui.ext

import co.rexiox.sentinel.ui.resources.Res
import co.rexiox.sentinel.ui.resources.ic_sentinel
import co.rexiox.sentinel.ui.resources.ic_sentinel_dark
import co.rexiox.sentinel.ui.resources.ic_sentinel_risk_low
import co.rexiox.sentinel.ui.resources.ic_sentinel_risk_medium
import co.rexiox.sentinel.ui.resources.ic_sentinel_risk_safe
import org.jetbrains.compose.resources.DrawableResource
import sentinel.core.type.RiskLevel

fun RiskLevel?.getSentinelRiskLogo(): DrawableResource = when (this) {
    RiskLevel.SAFE -> Res.drawable.ic_sentinel_risk_safe
    RiskLevel.LOW -> Res.drawable.ic_sentinel_risk_low
    RiskLevel.MEDIUM -> Res.drawable.ic_sentinel_risk_medium
    RiskLevel.HIGH -> Res.drawable.ic_sentinel
    null -> Res.drawable.ic_sentinel_dark
}