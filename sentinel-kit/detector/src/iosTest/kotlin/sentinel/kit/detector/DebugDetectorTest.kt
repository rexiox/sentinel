package sentinel.kit.detector

import dev.mokkery.answering.returns
import dev.mokkery.everySuspend
import dev.mokkery.mock
import dev.mokkery.verify
import sentinel.core.detector.Threat
import sentinel.core.violation.IosViolation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class DebugDetectorTest {

    @Test
    fun `detect should return DebuggerAttached when debugger is attached`() {
        val mockDetector = mock<DebugDetector> {
            everySuspend { detect() } returns listOf(
                Threat(violation = IosViolation.Debugger.DebuggerAttached)
            )
        }

        val threats = mockDetector.detect()

        assertEquals(1, threats.size)
        assertTrue(threats.first().violation is IosViolation.Debugger.DebuggerAttached)

        verify { mockDetector.detect() }
    }

    @Test
    fun `detect should return empty list when no debugger is attached`() {
        val mockDetector = mock<DebugDetector> {
            everySuspend { detect() } returns emptyList()
        }

        val threats = mockDetector.detect()

        assertTrue(threats.isEmpty())

        verify { mockDetector.detect() }
    }
}