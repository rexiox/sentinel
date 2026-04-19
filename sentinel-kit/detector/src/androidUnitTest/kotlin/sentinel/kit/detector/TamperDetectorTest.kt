package sentinel.kit.detector

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import sentinel.core.violation.AndroidViolation

@RunWith(RobolectricTestRunner::class)
class TamperDetectorTest {

    private lateinit var context: Context
    private val validAppId = listOf(1.toByte(), 2.toByte(), 3.toByte())
    private val validAppIntegrity = listOf(4.toByte(), 5.toByte(), 6.toByte())

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `detect should return empty list when package and signature are valid`() {
        val detector = createDetector(
            appId = validAppId,
            appIntegrity = validAppIntegrity,
            packageValid = true,
            signatureValid = true
        )

        val result = detector.detect()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `detect should identify package name changed when package invalid`() {
        val detector = createDetector(
            appId = validAppId,
            appIntegrity = validAppIntegrity,
            packageValid = false,
            signatureValid = true
        )

        val result = detector.detect()

        assertEquals(1, result.size)
        assertTrue(result.first().violation is AndroidViolation.Tamper.PackageNameChanged)
    }

    @Test
    fun `detect should identify signature mismatch when signature invalid`() {
        val detector = createDetector(
            appId = validAppId,
            appIntegrity = validAppIntegrity,
            packageValid = true,
            signatureValid = false
        )

        val result = detector.detect()

        assertEquals(1, result.size)
        assertTrue(result.first().violation is AndroidViolation.Tamper.SignatureMismatch)
    }

    @Test
    fun `detect should identify both threats when package and signature invalid`() {
        val detector = createDetector(
            appId = validAppId,
            appIntegrity = validAppIntegrity,
            packageValid = false,
            signatureValid = false
        )

        val result = detector.detect()

        assertEquals(2, result.size)
        assertTrue(result.any { it.violation is AndroidViolation.Tamper.PackageNameChanged })
        assertTrue(result.any { it.violation is AndroidViolation.Tamper.SignatureMismatch })
    }

    @Test
    fun `detect should handle null appId`() {
        val detector = createDetector(
            appId = null,
            appIntegrity = validAppIntegrity,
            packageValid = false,
            signatureValid = false
        )

        val result = detector.detect()

        assertEquals(2, result.size)
    }

    @Test
    fun `detect should handle null appIntegrity`() {
        val detector = createDetector(
            appId = validAppId,
            appIntegrity = null,
            packageValid = false,
            signatureValid = false
        )

        val result = detector.detect()

        assertEquals(2, result.size)
    }

    @Test
    fun `detect should handle both null appId and appIntegrity`() {
        val detector = createDetector(
            appId = null,
            appIntegrity = null,
            packageValid = false,
            signatureValid = false
        )

        val result = detector.detect()

        assertEquals(2, result.size)
    }

    @Test
    fun `verifyPackage should receive correct parameters`() {
        var receivedContext: Context? = null
        var receivedPackage: ByteArray? = null

        val detector = object : TamperDetector(context, validAppId, validAppIntegrity) {
            override fun loadLibrary() = Unit

            override fun verifyPackage(context: Context, expectedPackage: ByteArray): Boolean {
                receivedContext = context
                receivedPackage = expectedPackage
                return true
            }

            override fun verifySignature(
                context: Context,
                expectedPackage: ByteArray,
                expectedPackageSignature: ByteArray,
            ): Boolean = true
        }

        detector.detect()

        assertEquals(this.context, receivedContext)
        assertTrue(receivedPackage.contentEquals(validAppId.toByteArray()))
    }

    @Test
    fun `verifySignature should receive correct parameters`() {
        var receivedContext: Context? = null
        var receivedPackage: ByteArray? = null
        var receivedSignature: ByteArray? = null

        val detector = object : TamperDetector(context, validAppId, validAppIntegrity) {
            override fun loadLibrary() = Unit

            override fun verifyPackage(context: Context, expectedPackage: ByteArray): Boolean = true

            override fun verifySignature(
                context: Context,
                expectedPackage: ByteArray,
                expectedPackageSignature: ByteArray,
            ): Boolean {
                receivedContext = context
                receivedPackage = expectedPackage
                receivedSignature = expectedPackageSignature
                return true
            }
        }

        detector.detect()

        assertEquals(this.context, receivedContext)
        assertTrue(receivedPackage.contentEquals(validAppId.toByteArray()))
        assertTrue(receivedSignature.contentEquals(validAppIntegrity.toByteArray()))
    }

    @Test
    fun `verifyPackage should handle empty appId`() {
        var receivedPackage: ByteArray? = null

        val detector = object : TamperDetector(context, emptyList(), validAppIntegrity) {
            override fun loadLibrary() = Unit

            override fun verifyPackage(context: Context, expectedPackage: ByteArray): Boolean {
                receivedPackage = expectedPackage
                return true
            }

            override fun verifySignature(
                context: Context,
                expectedPackage: ByteArray,
                expectedPackageSignature: ByteArray,
            ): Boolean = true
        }

        detector.detect()

        assertTrue(receivedPackage.contentEquals(ByteArray(0)))
    }

    @Test
    fun `verifySignature should handle empty appIntegrity`() {
        var receivedSignature: ByteArray? = null

        val detector = object : TamperDetector(context, validAppId, emptyList()) {
            override fun loadLibrary() = Unit

            override fun verifyPackage(context: Context, expectedPackage: ByteArray): Boolean = true

            override fun verifySignature(
                context: Context,
                expectedPackage: ByteArray,
                expectedPackageSignature: ByteArray,
            ): Boolean {
                receivedSignature = expectedPackageSignature
                return true
            }
        }

        detector.detect()

        assertTrue(receivedSignature.contentEquals(ByteArray(0)))
    }

    private fun createDetector(
        appId: List<Byte>?,
        appIntegrity: List<Byte>?,
        packageValid: Boolean,
        signatureValid: Boolean,
    ): TamperDetector {
        return object : TamperDetector(context, appId, appIntegrity) {
            override fun loadLibrary() = Unit

            override fun verifyPackage(context: Context, expectedPackage: ByteArray): Boolean =
                packageValid

            override fun verifySignature(
                context: Context,
                expectedPackage: ByteArray,
                expectedPackageSignature: ByteArray,
            ): Boolean = signatureValid
        }
    }
}