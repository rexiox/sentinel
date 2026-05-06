package sentinel.kit.detector

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import sentinel.core.violation.AndroidViolation

@RunWith(RobolectricTestRunner::class)
class EmulatorDetectorTest {

    @Test
    fun `detect should return empty list when no emulator detected`() {
        val detector = createDetector(reason = null)

        val result = detector.detect()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `detect should return empty list when reason is blank`() {
        val detector = createDetector(reason = "  ")

        val result = detector.detect()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `detect should identify emulator when reason is provided`() {
        val expectedReason = "Genymotion"
        val detector = createDetector(reason = expectedReason)

        val result = detector.detect()

        assertEquals(1, result.size)
        val violation = result.first().violation as AndroidViolation.Emulator.Detected
        assertEquals(expectedReason, violation.name)
    }

    @Test
    fun `checkEmulatorReason should be called during detect`() {
        var isMethodCalled = false

        val detector = object : EmulatorDetector() {
            override fun loadLibrary() = Unit
            override fun getEmulatorDetectionReason(): String? {
                isMethodCalled = true
                return null
            }
        }

        detector.detect()
        assertTrue(isMethodCalled)
    }

    private fun createDetector(reason: String?): EmulatorDetector = object : EmulatorDetector() {
        override fun loadLibrary() = Unit
        override fun getEmulatorDetectionReason(): String? = reason
    }
}