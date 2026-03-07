package sentinel.kit.detector

import android.content.Context
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.AndroidViolation

class TamperDetector(
    private val context: Context,
    private val appId: List<Byte>?,
    private val appSignature: List<Byte>?,
) : SecurityDetector {

    init {
        System.loadLibrary("sentinel-tamper")
    }

    private external fun verifyPackage(
        context: Context,
        expectedPackage: ByteArray,
    ): Boolean

    private external fun verifySignature(
        context: Context,
        expectedPackage: ByteArray,
        expectedPackageSignature: ByteArray,
    ): Boolean

    override fun detect(): List<Threat> = buildList {
        if (!isPackageValid()) {
            add(element = Threat(violation = AndroidViolation.Tamper.PackageNameChanged))
        }

        if (!isSignatureValid()) {
            add(element = Threat(violation = AndroidViolation.Tamper.SignatureMismatch))
        }
    }

    private fun isPackageValid(): Boolean = verifyPackage(
        context = context,
        expectedPackage = appId.orEmpty().toByteArray()
    )

    private fun isSignatureValid(): Boolean = verifySignature(
        context = context,
        expectedPackage = appId.orEmpty().toByteArray(),
        expectedPackageSignature = appSignature.orEmpty().toByteArray()
    )
}