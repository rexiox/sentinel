package sentinel.attest.result

import sentinel.core.report.SecurityReport
import kotlinx.serialization.Serializable

@Serializable
data class AttestationPayload(
    val nonce: String,
    val attestationToken: String,
    val securityReport: SecurityReportDto,
    val platform: String,
    val timestamp: Long,
)

@Serializable
data class SecurityReportDto(
    val severity: Int,
    val riskLevel: String,
    val isCompromised: Boolean,
    val isTampered: Boolean,
    val isHooked: Boolean,
    val isVirtualDevice: Boolean,
    val isDebugged: Boolean,
    val isMockLocation: Boolean,
    val threats: List<String>,
    val timestamp: Long,
)

fun SecurityReport.toDto() = SecurityReportDto(
    severity = severity,
    riskLevel = riskLevel.name,
    isCompromised = isCompromised,
    isTampered = isTampered,
    isHooked = isHooked,
    isVirtualDevice = isEmulator || isSimulator,
    isDebugged = isDebugged,
    isMockLocation = isMockLocation,
    threats = threats.map { threat -> threat.violation::class.simpleName.toString() },
    timestamp = this.timestamp
)