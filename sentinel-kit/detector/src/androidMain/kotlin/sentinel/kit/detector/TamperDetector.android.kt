package sentinel.kit.detector

import android.content.Context
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.handler.ExceptionHandler
import sentinel.core.violation.AndroidViolation

open class TamperDetector(
    private val context: Context,
    private val appId: List<Byte>?,
    private val appIntegrity: List<Byte>?,
) : SecurityDetector {

    init {
        ExceptionHandler.safely(context = "TamperDetector.init") {
            loadLibrary()
        }
    }

    open fun loadLibrary() {
        System.loadLibrary("sentinel-tamper")
    }

    open external fun verifyPackage(
        context: Context,
        expectedPackage: ByteArray,
    ): Boolean

    open external fun verifySignature(
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
        expectedPackageSignature = appIntegrity.orEmpty().toByteArray()
    )
}