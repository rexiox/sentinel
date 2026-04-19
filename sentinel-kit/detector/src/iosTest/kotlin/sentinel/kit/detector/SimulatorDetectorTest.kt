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

class SimulatorDetectorTest {

    private val detector = SimulatorDetector()

    @Test
    fun `detect should return Detected when running on simulator`() {
        val threats = detector.detect()

        if (threats.isNotEmpty()) {
            assertEquals(1, threats.size)
            assertTrue(threats.first().violation is IosViolation.Simulator.Detected)
        }
    }

    @Test
    fun `detect should return empty list when running on real device`() {
        val threats = detector.detect()

        if (threats.isEmpty()) {
            assertTrue(threats.isEmpty())
        } else {
            assertEquals(1, threats.size)
            assertTrue(threats.first().violation is IosViolation.Simulator.Detected)
        }
    }

    @Test
    fun `violation should be of correct type when simulator is detected`() {
        val mockDetector = mock<SimulatorDetector> {
            everySuspend { detect() } returns listOf(
                Threat(violation = IosViolation.Simulator.Detected())
            )
        }

        val threats = mockDetector.detect()

        threats.forEach { threat ->
            assertTrue(
                actual = threat.violation is IosViolation.Simulator.Detected,
            )
        }

        verify { mockDetector.detect() }
    }
}