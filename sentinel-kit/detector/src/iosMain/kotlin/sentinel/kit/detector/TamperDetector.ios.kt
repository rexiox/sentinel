@file:OptIn(ExperimentalForeignApi::class)

package sentinel.kit.detector

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.IosViolation
import sentinel.detector.verifyBundleId
import sentinel.detector.verifyProvisioningHash

open class TamperDetector(
    open val bundleId: List<Byte>?,
    open val appIntegrity: List<Byte>?,
) : SecurityDetector {

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
                add(element = Threat(violation = IosViolation.Tamper.ProvisioningHashMismatch))
            }
        }
    }
}