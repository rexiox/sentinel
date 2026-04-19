package sentinel.kit.detector

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import sentinel.core.violation.AndroidViolation

@RunWith(RobolectricTestRunner::class)
class HookDetectorTest {

    @Test
    fun `detect should return empty list when no hooks detected`() {
        val detector = createDetector(
            fridaDetected = false,
            stackTraceName = null
        )

        val result = detector.detect()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `detect should identify Frida when detected`() {
        val detector = createDetector(
            fridaDetected = true,
            stackTraceName = null
        )

        val result = detector.detect()

        assertEquals(1, result.size)
        val violation = result.first().violation as AndroidViolation.Hook.FrameworkDetected
        assertEquals("Frida", violation.name)
    }

    @Test
    fun `detect should identify hook framework from stack trace`() {
        val detector = createDetector(
            fridaDetected = false,
            stackTraceName = "Xposed"
        )

        val result = detector.detect()

        assertEquals(1, result.size)
        val violation = result.first().violation as AndroidViolation.Hook.FrameworkDetected
        assertEquals("Xposed", violation.name)
    }

    @Test
    fun `detect should identify both Frida and stack trace framework`() {
        val detector = createDetector(
            fridaDetected = true,
            stackTraceName = "LSPosed"
        )

        val result = detector.detect()

        assertEquals(2, result.size)
        assertTrue(result.any { (it.violation as AndroidViolation.Hook.FrameworkDetected).name == "Frida" })
        assertTrue(result.any { (it.violation as AndroidViolation.Hook.FrameworkDetected).name == "LSPosed" })
    }

    @Test
    fun `detect should handle different stack trace framework names`() {
        val frameworkNames = listOf("Xposed", "EdXposed", "LSPosed", "Substrate", "TaiChi")

        frameworkNames.forEach { frameworkName ->
            val detector = createDetector(
                fridaDetected = false,
                stackTraceName = frameworkName
            )

            val result = detector.detect()

            assertEquals(1, result.size)
            val violation = result.first().violation as AndroidViolation.Hook.FrameworkDetected
            assertEquals(frameworkName, violation.name)
        }
    }

    @Test
    fun `checkStackTraceManually should be called during detect`() {
        var checkStackTraceCalled = false

        val detector = object : HookDetector() {
            override fun loadLibrary() = Unit

            override fun isFridaDetected(): Boolean = false

            override fun checkStackTraceManually(): String? {
                checkStackTraceCalled = true
                return null
            }
        }

        detector.detect()

        assertTrue(checkStackTraceCalled)
    }

    @Test
    fun `isFridaDetected should be called during detect`() {
        var isFridaDetectedCalled = false

        val detector = object : HookDetector() {
            override fun loadLibrary() = Unit

            override fun isFridaDetected(): Boolean {
                isFridaDetectedCalled = true
                return false
            }

            override fun checkStackTraceManually(): String? = null
        }

        detector.detect()

        assertTrue(isFridaDetectedCalled)
    }

    private fun createDetector(
        fridaDetected: Boolean,
        stackTraceName: String?,
    ): HookDetector {
        return object : HookDetector() {
            override fun loadLibrary() = Unit
            override fun isFridaDetected(): Boolean = fridaDetected
            override fun checkStackTraceManually(): String? = stackTraceName
        }
    }
}