package sentinel.kit.detector

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import sentinel.core.detector.Threat
import sentinel.core.violation.AndroidViolation

@RunWith(RobolectricTestRunner::class)
class DebugDetectorTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `detect should identify debugger attached threat`() {
        val detector = createDetector(
            debuggerAttached = true,
            packageDebuggable = false,
            testKeys = false
        )

        val result = detector.detect()

        assertEquals(1, result.size)
        assertTrue(result.containsViolation(AndroidViolation.Debugger.DebuggerAttached::class.java))
        assertFalse(result.containsViolation(AndroidViolation.Debugger.Debuggable::class.java))
        assertFalse(result.containsViolation(AndroidViolation.Debugger.TestKeys::class.java))
    }

    @Test
    fun `detect should identify package debuggable threat`() {
        val detector = createDetector(
            debuggerAttached = false,
            packageDebuggable = true,
            testKeys = false
        )

        val result = detector.detect()

        assertEquals(1, result.size)
        assertFalse(result.containsViolation(AndroidViolation.Debugger.DebuggerAttached::class.java))
        assertTrue(result.containsViolation(AndroidViolation.Debugger.Debuggable::class.java))
        assertFalse(result.containsViolation(AndroidViolation.Debugger.TestKeys::class.java))
    }

    @Test
    fun `detect should identify test keys threat`() {
        val detector = createDetector(
            debuggerAttached = false,
            packageDebuggable = false,
            testKeys = true
        )

        val result = detector.detect()

        assertEquals(1, result.size)
        assertFalse(result.containsViolation(AndroidViolation.Debugger.DebuggerAttached::class.java))
        assertFalse(result.containsViolation(AndroidViolation.Debugger.Debuggable::class.java))
        assertTrue(result.containsViolation(AndroidViolation.Debugger.TestKeys::class.java))
    }

    @Test
    fun `detect should identify multiple threats when all conditions are met`() {
        val detector = createDetector(
            debuggerAttached = true,
            packageDebuggable = true,
            testKeys = true
        )

        val result = detector.detect()

        assertEquals(3, result.size)
        assertTrue(result.containsViolation(AndroidViolation.Debugger.DebuggerAttached::class.java))
        assertTrue(result.containsViolation(AndroidViolation.Debugger.Debuggable::class.java))
        assertTrue(result.containsViolation(AndroidViolation.Debugger.TestKeys::class.java))
    }

    @Test
    fun `detect should return empty list when no threats detected`() {
        val detector = createDetector(
            debuggerAttached = false,
            packageDebuggable = false,
            testKeys = false
        )

        val result = detector.detect()

        assertTrue(result.isEmpty())
        assertEquals(0, result.size)
    }

    @Test
    fun `detect should identify debugger and debuggable threats only`() {
        val detector = createDetector(
            debuggerAttached = true,
            packageDebuggable = true,
            testKeys = false
        )

        val result = detector.detect()

        assertEquals(2, result.size)
        assertTrue(result.containsViolation(AndroidViolation.Debugger.DebuggerAttached::class.java))
        assertTrue(result.containsViolation(AndroidViolation.Debugger.Debuggable::class.java))
        assertFalse(result.containsViolation(AndroidViolation.Debugger.TestKeys::class.java))
    }

    @Test
    fun `detect should identify debugger and test keys threats only`() {
        val detector = createDetector(
            debuggerAttached = true,
            packageDebuggable = false,
            testKeys = true
        )

        val result = detector.detect()

        assertEquals(2, result.size)
        assertTrue(result.containsViolation(AndroidViolation.Debugger.DebuggerAttached::class.java))
        assertFalse(result.containsViolation(AndroidViolation.Debugger.Debuggable::class.java))
        assertTrue(result.containsViolation(AndroidViolation.Debugger.TestKeys::class.java))
    }

    @Test
    fun `detect should identify debuggable and test keys threats only`() {
        val detector = createDetector(
            debuggerAttached = false,
            packageDebuggable = true,
            testKeys = true
        )

        val result = detector.detect()

        assertEquals(2, result.size)
        assertFalse(result.containsViolation(AndroidViolation.Debugger.DebuggerAttached::class.java))
        assertTrue(result.containsViolation(AndroidViolation.Debugger.Debuggable::class.java))
        assertTrue(result.containsViolation(AndroidViolation.Debugger.TestKeys::class.java))
    }

    @Test
    fun `isDebuggerAttached should be called during detect`() {
        var isDebuggerAttachedCalled = false

        val detector = object : DebugDetector(context) {
            override fun loadLibrary() = Unit

            override fun isDebuggerAttached(): Boolean {
                isDebuggerAttachedCalled = true
                return false
            }

            override fun isPackageDebuggable(context: Context): Boolean = false
            override fun checkTestKeys(): Boolean = false
        }

        detector.detect()

        assertTrue(isDebuggerAttachedCalled)
    }

    @Test
    fun `isPackageDebuggable should be called during detect`() {
        var isPackageDebuggableCalled = false

        val detector = object : DebugDetector(context) {
            override fun loadLibrary() = Unit
            override fun isDebuggerAttached(): Boolean = false

            override fun isPackageDebuggable(context: Context): Boolean {
                isPackageDebuggableCalled = true
                return false
            }

            override fun checkTestKeys(): Boolean = false
        }

        detector.detect()

        assertTrue(isPackageDebuggableCalled)
    }

    @Test
    fun `checkTestKeys should be called during detect`() {
        var checkTestKeysCalled = false

        val detector = object : DebugDetector(context) {
            override fun loadLibrary() = Unit
            override fun isDebuggerAttached(): Boolean = false
            override fun isPackageDebuggable(context: Context): Boolean = false

            override fun checkTestKeys(): Boolean {
                checkTestKeysCalled = true
                return false
            }
        }

        detector.detect()

        assertTrue(checkTestKeysCalled)
    }

    @Test
    fun `detect should pass correct context to isPackageDebuggable`() {
        var receivedContext: Context? = null

        val detector = object : DebugDetector(context) {
            override fun loadLibrary() = Unit
            override fun isDebuggerAttached(): Boolean = false

            override fun isPackageDebuggable(context: Context): Boolean {
                receivedContext = context
                return false
            }

            override fun checkTestKeys(): Boolean = false
        }

        detector.detect()

        assertEquals(context, receivedContext)
    }

    private fun createDetector(
        debuggerAttached: Boolean,
        packageDebuggable: Boolean,
        testKeys: Boolean,
    ): DebugDetector {
        return object : DebugDetector(context) {
            override fun loadLibrary() = Unit
            override fun isDebuggerAttached(): Boolean = debuggerAttached
            override fun isPackageDebuggable(context: Context): Boolean = packageDebuggable
            override fun checkTestKeys(): Boolean = testKeys
        }
    }

    private fun List<Threat>.containsViolation(violationClass: Class<*>): Boolean {
        return any { it.violation::class.java == violationClass }
    }
}