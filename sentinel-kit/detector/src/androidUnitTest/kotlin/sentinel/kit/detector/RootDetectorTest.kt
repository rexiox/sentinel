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
class RootDetectorTest {

    private lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `detect should return empty list when no root indicators found`() {
        val detector = createDetector(
            appsInstalled = false,
            binariesFound = false,
            suspiciousMounts = false,
            suCommandExecuted = false
        )

        val result = detector.detect()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `detect should identify root apps when installed`() {
        val detector = createDetector(
            appsInstalled = true,
            binariesFound = false,
            suspiciousMounts = false,
            suCommandExecuted = false
        )

        val result = detector.detect()

        assertEquals(1, result.size)
        assertTrue(result.first().violation is AndroidViolation.Root.AppInstalled)
    }

    @Test
    fun `detect should identify su binary when found`() {
        val detector = createDetector(
            appsInstalled = false,
            binariesFound = true,
            suspiciousMounts = false,
            suCommandExecuted = false
        )

        val result = detector.detect()

        assertEquals(1, result.size)
        assertTrue(result.first().violation is AndroidViolation.Root.SuBinaryFound)
    }

    @Test
    fun `detect should identify suspicious mounts when found`() {
        val detector = createDetector(
            appsInstalled = false,
            binariesFound = false,
            suspiciousMounts = true,
            suCommandExecuted = false
        )

        val result = detector.detect()

        assertEquals(1, result.size)
        assertTrue(result.first().violation is AndroidViolation.Root.SuspiciousMount)
    }

    @Test
    fun `detect should identify su command execution`() {
        val detector = createDetector(
            appsInstalled = false,
            binariesFound = false,
            suspiciousMounts = false,
            suCommandExecuted = true
        )

        val result = detector.detect()

        assertEquals(1, result.size)
        assertTrue(result.first().violation is AndroidViolation.Root.SuCommandExecuted)
    }

    @Test
    fun `detect should identify all root indicators when all present`() {
        val detector = createDetector(
            appsInstalled = true,
            binariesFound = true,
            suspiciousMounts = true,
            suCommandExecuted = true
        )

        val result = detector.detect()

        assertEquals(4, result.size)
        assertTrue(result.any { it.violation is AndroidViolation.Root.AppInstalled })
        assertTrue(result.any { it.violation is AndroidViolation.Root.SuBinaryFound })
        assertTrue(result.any { it.violation is AndroidViolation.Root.SuspiciousMount })
        assertTrue(result.any { it.violation is AndroidViolation.Root.SuCommandExecuted })
    }

    @Test
    fun `checkApps should receive correct context`() {
        var receivedContext: Context? = null

        val detector = object : RootDetector(context) {
            override fun loadLibrary() = Unit

            override fun checkApps(context: Context): Boolean {
                receivedContext = context
                return false
            }

            override fun checkBinaries(): Boolean = false
            override fun checkMounts(): Boolean = false
            override fun checkSuCommand(): Boolean = false
        }

        detector.detect()

        assertEquals(context, receivedContext)
    }

    private fun createDetector(
        appsInstalled: Boolean,
        binariesFound: Boolean,
        suspiciousMounts: Boolean,
        suCommandExecuted: Boolean,
    ): RootDetector {
        return object : RootDetector(context) {
            override fun loadLibrary() = Unit
            override fun checkApps(context: Context): Boolean = appsInstalled
            override fun checkBinaries(): Boolean = binariesFound
            override fun checkMounts(): Boolean = suspiciousMounts
            override fun checkSuCommand(): Boolean = suCommandExecuted
        }
    }
}