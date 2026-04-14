package sentinel.kit.detector

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.IosViolation
import sentinel.detector.verifyBundleId
import sentinel.detector.verifyProvisioningHash

class TamperDetector(
    val bundleId: List<Byte>?,
    val appIntegrity: List<Byte>?,
) : SecurityDetector {

    @OptIn(ExperimentalForeignApi::class)
    override fun detect(): List<Threat> = buildList {
        val bundleBytes = bundleId?.toByteArray() ?: byteArrayOf()
        val hashBytes = appIntegrity?.toByteArray() ?: byteArrayOf()

        if (bundleBytes.isNotEmpty()) {
            if (!verifyBundleId(bundleIdData = bundleBytes.refTo(0), length = bundleBytes.size)) {
                add(element = Threat(violation = IosViolation.Tamper.BundleIdChanged))
            }
        }

        if (hashBytes.isNotEmpty()) {
            if (!verifyProvisioningHash(hashData = hashBytes.refTo(0), length = hashBytes.size)) {
                add(element = Threat(violation = IosViolation.Tamper.SignatureMismatch))
            }
        }
    }
}