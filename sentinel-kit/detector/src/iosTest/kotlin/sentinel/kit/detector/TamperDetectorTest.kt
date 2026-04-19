package sentinel.kit.detector

import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import dev.mokkery.verify
import sentinel.core.detector.Threat
import sentinel.core.violation.IosViolation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TamperDetectorTest {

    @Test
    fun `detect should return empty list when bundle and provisioning are valid`() {
        val mockDetector = mock<TamperDetector> {
            every { detect() } returns emptyList()
        }

        val result = mockDetector.detect()

        assertTrue(result.isEmpty())
        verify { mockDetector.detect() }
    }

    @Test
    fun `detect should identify bundle id changed when bundle invalid`() {
        val mockDetector = mock<TamperDetector> {
            every { detect() } returns listOf(
                Threat(violation = IosViolation.Tamper.BundleIdChanged)
            )
        }

        val result = mockDetector.detect()

        assertEquals(1, result.size)
        assertTrue(result.first().violation is IosViolation.Tamper.BundleIdChanged)
        verify { mockDetector.detect() }
    }

    @Test
    fun `detect should identify provisioning hash mismatch when hash invalid`() {
        val mockDetector = mock<TamperDetector> {
            every { detect() } returns listOf(
                Threat(violation = IosViolation.Tamper.ProvisioningHashMismatch)
            )
        }

        val result = mockDetector.detect()

        assertEquals(1, result.size)
        assertTrue(result.first().violation is IosViolation.Tamper.ProvisioningHashMismatch)
        verify { mockDetector.detect() }
    }

    @Test
    fun `detect should identify both threats when bundle and hash invalid`() {
        val mockDetector = mock<TamperDetector> {
            every { detect() } returns listOf(
                Threat(violation = IosViolation.Tamper.BundleIdChanged),
                Threat(violation = IosViolation.Tamper.ProvisioningHashMismatch)
            )
        }

        val result = mockDetector.detect()

        assertEquals(2, result.size)
        assertTrue(result.any { it.violation is IosViolation.Tamper.BundleIdChanged })
        assertTrue(result.any { it.violation is IosViolation.Tamper.ProvisioningHashMismatch })
        verify { mockDetector.detect() }
    }

    @Test
    fun `detect should handle null bundleId`() {
        val mockDetector = mock<TamperDetector> {
            every { detect() } returns emptyList()
        }

        val result = mockDetector.detect()

        assertTrue(result.isEmpty())
        verify { mockDetector.detect() }
    }

    @Test
    fun `detect should handle null appIntegrity`() {
        val mockDetector = mock<TamperDetector> {
            every { detect() } returns emptyList()
        }

        val result = mockDetector.detect()

        assertTrue(result.isEmpty())
        verify { mockDetector.detect() }
    }

    @Test
    fun `detect should handle both null bundleId and appIntegrity`() {
        val mockDetector = mock<TamperDetector> {
            every { detect() } returns emptyList()
        }

        val result = mockDetector.detect()

        assertTrue(result.isEmpty())
        verify { mockDetector.detect() }
    }

    @Test
    fun `detect should handle empty bundleId list`() {
        val mockDetector = mock<TamperDetector> {
            every { detect() } returns emptyList()
        }

        val result = mockDetector.detect()

        assertTrue(result.isEmpty())
        verify { mockDetector.detect() }
    }

    @Test
    fun `detect should handle empty appIntegrity list`() {
        val mockDetector = mock<TamperDetector> {
            every { detect() } returns emptyList()
        }

        val result = mockDetector.detect()

        assertTrue(result.isEmpty())
        verify { mockDetector.detect() }
    }
}