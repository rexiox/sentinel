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

class JailbreakDetectorTest {

    @Test
    fun `detect should return empty list when no jailbreak detected`() {
        val mockDetector = mock<JailbreakDetector> {
            every { detect() } returns emptyList()
        }

        val result = mockDetector.detect()

        assertTrue(result.isEmpty())
        verify { mockDetector.detect() }
    }

    @Test
    fun `detect should identify sandbox violation when sandbox check fails`() {
        val mockDetector = mock<JailbreakDetector> {
            every { detect() } returns listOf(
                Threat(violation = IosViolation.Jailbreak.Sandbox())
            )
        }

        val result = mockDetector.detect()

        assertEquals(1, result.size)
        assertTrue(result.first().violation is IosViolation.Jailbreak.Sandbox)
        verify { mockDetector.detect() }
    }

    @Test
    fun `detect should identify system paths violation when system paths check fails`() {
        val mockDetector = mock<JailbreakDetector> {
            every { detect() } returns listOf(
                Threat(violation = IosViolation.Jailbreak.SystemPaths())
            )
        }

        val result = mockDetector.detect()

        assertEquals(1, result.size)
        assertTrue(result.first().violation is IosViolation.Jailbreak.SystemPaths)
        verify { mockDetector.detect() }
    }

    @Test
    fun `detect should identify suspicious symlinks when symlink check fails`() {
        val mockDetector = mock<JailbreakDetector> {
            every { detect() } returns listOf(
                Threat(violation = IosViolation.Jailbreak.SuspiciousSymlinks())
            )
        }

        val result = mockDetector.detect()

        assertEquals(1, result.size)
        assertTrue(result.first().violation is IosViolation.Jailbreak.SuspiciousSymlinks)
        verify { mockDetector.detect() }
    }

    @Test
    fun `detect should identify jailbreak apps when forbidden apps found`() {
        val mockDetector = mock<JailbreakDetector> {
            every { detect() } returns listOf(
                Threat(violation = IosViolation.Jailbreak.AppInstalled())
            )
        }

        val result = mockDetector.detect()

        assertEquals(1, result.size)
        assertTrue(result.first().violation is IosViolation.Jailbreak.AppInstalled)
        verify { mockDetector.detect() }
    }

    @Test
    fun `detect should identify url schemes when jailbreak schemes found`() {
        val mockDetector = mock<JailbreakDetector> {
            every { detect() } returns listOf(
                Threat(violation = IosViolation.Jailbreak.URLSchemes())
            )
        }

        val result = mockDetector.detect()

        assertEquals(1, result.size)
        assertTrue(result.first().violation is IosViolation.Jailbreak.URLSchemes)
        verify { mockDetector.detect() }
    }

    @Test
    fun `detect should identify all jailbreak threats when all checks triggered`() {
        val mockDetector = mock<JailbreakDetector> {
            every { detect() } returns listOf(
                Threat(violation = IosViolation.Jailbreak.Sandbox()),
                Threat(violation = IosViolation.Jailbreak.SystemPaths()),
                Threat(violation = IosViolation.Jailbreak.SuspiciousSymlinks()),
                Threat(violation = IosViolation.Jailbreak.AppInstalled()),
                Threat(violation = IosViolation.Jailbreak.URLSchemes())
            )
        }

        val result = mockDetector.detect()

        assertEquals(5, result.size)
        assertTrue(result.any { it.violation is IosViolation.Jailbreak.Sandbox })
        assertTrue(result.any { it.violation is IosViolation.Jailbreak.SystemPaths })
        assertTrue(result.any { it.violation is IosViolation.Jailbreak.SuspiciousSymlinks })
        assertTrue(result.any { it.violation is IosViolation.Jailbreak.AppInstalled })
        assertTrue(result.any { it.violation is IosViolation.Jailbreak.URLSchemes })
        verify { mockDetector.detect() }
    }
}