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

class HookDetectorTest {

    @Test
    fun `detect should return empty list when no hooks detected`() {
        val mockDetector = mock<HookDetector> {
            every { detect() } returns emptyList()
        }

        val result = mockDetector.detect()

        assertTrue(result.isEmpty())
        verify { mockDetector.detect() }
    }

    @Test
    fun `detect should identify Frida when loaded images detected`() {
        val mockDetector = mock<HookDetector> {
            every { detect() } returns listOf(
                Threat(violation = IosViolation.Hook.FrameworkDetected(name = "Frida"))
            )
        }

        val result = mockDetector.detect()

        assertEquals(1, result.size)
        val violation = result.first().violation as IosViolation.Hook.FrameworkDetected
        assertEquals("Frida", violation.name)
        verify { mockDetector.detect() }
    }

    @Test
    fun `detect should identify Frida-Memory when memory signatures detected`() {
        val mockDetector = mock<HookDetector> {
            every { detect() } returns listOf(
                Threat(violation = IosViolation.Hook.FrameworkDetected(name = "Frida-Memory"))
            )
        }

        val result = mockDetector.detect()

        assertEquals(1, result.size)
        val violation = result.first().violation as IosViolation.Hook.FrameworkDetected
        assertEquals("Frida-Memory", violation.name)
        verify { mockDetector.detect() }
    }

    @Test
    fun `detect should identify Frida-Port when reserved port detected`() {
        val mockDetector = mock<HookDetector> {
            every { detect() } returns listOf(
                Threat(violation = IosViolation.Hook.FrameworkDetected(name = "Frida-Port"))
            )
        }

        val result = mockDetector.detect()

        assertEquals(1, result.size)
        val violation = result.first().violation as IosViolation.Hook.FrameworkDetected
        assertEquals("Frida-Port", violation.name)
        verify { mockDetector.detect() }
    }

    @Test
    fun `detect should identify inline hook when system function tampered`() {
        val mockDetector = mock<HookDetector> {
            every { detect() } returns listOf(
                Threat(violation = IosViolation.Hook.InlineHookDetected(name = "open"))
            )
        }

        val result = mockDetector.detect()

        assertEquals(1, result.size)
        val violation = result.first().violation as IosViolation.Hook.InlineHookDetected
        assertEquals("open", violation.name)
        verify { mockDetector.detect() }
    }

    @Test
    fun `detect should identify multiple inline hooks when multiple functions tampered`() {
        val mockDetector = mock<HookDetector> {
            every { detect() } returns listOf(
                Threat(violation = IosViolation.Hook.InlineHookDetected(name = "open")),
                Threat(violation = IosViolation.Hook.InlineHookDetected(name = "fopen"))
            )
        }

        val result = mockDetector.detect()

        assertEquals(2, result.size)
        assertTrue(result.any {
            (it.violation as IosViolation.Hook.InlineHookDetected).name == "open"
        })
        assertTrue(result.any {
            (it.violation as IosViolation.Hook.InlineHookDetected).name == "fopen"
        })
        verify { mockDetector.detect() }
    }

    @Test
    fun `detect should identify all threats when all detection methods triggered`() {
        val mockDetector = mock<HookDetector> {
            every { detect() } returns listOf(
                Threat(violation = IosViolation.Hook.FrameworkDetected(name = "Frida")),
                Threat(violation = IosViolation.Hook.FrameworkDetected(name = "Frida-Memory")),
                Threat(violation = IosViolation.Hook.FrameworkDetected(name = "Frida-Port")),
                Threat(violation = IosViolation.Hook.InlineHookDetected(name = "open"))
            )
        }

        val result = mockDetector.detect()

        assertEquals(4, result.size)
        assertTrue(result.any {
            (it.violation as? IosViolation.Hook.FrameworkDetected)?.name == "Frida"
        })
        assertTrue(result.any {
            (it.violation as? IosViolation.Hook.FrameworkDetected)?.name == "Frida-Memory"
        })
        assertTrue(result.any {
            (it.violation as? IosViolation.Hook.FrameworkDetected)?.name == "Frida-Port"
        })
        assertTrue(result.any {
            (it.violation as? IosViolation.Hook.InlineHookDetected)?.name == "open"
        })
        verify { mockDetector.detect() }
    }

    @Test
    fun `detect should handle combination of framework and inline hook detections`() {
        val mockDetector = mock<HookDetector> {
            every { detect() } returns listOf(
                Threat(violation = IosViolation.Hook.FrameworkDetected(name = "Frida")),
                Threat(violation = IosViolation.Hook.InlineHookDetected(name = "open")),
                Threat(violation = IosViolation.Hook.InlineHookDetected(name = "dlopen"))
            )
        }

        val result = mockDetector.detect()

        assertEquals(3, result.size)
        assertTrue(result.any { it.violation is IosViolation.Hook.FrameworkDetected })
        assertEquals(2, result.count { it.violation is IosViolation.Hook.InlineHookDetected })
        verify { mockDetector.detect() }
    }
}