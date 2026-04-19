package sentinel.kit.detector

import android.os.Build
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.util.ReflectionHelpers
import sentinel.core.violation.AndroidViolation

@RunWith(RobolectricTestRunner::class)
class EmulatorDetectorTest {

    private lateinit var detector: EmulatorDetector

    @Before
    fun setUp() {
        detector = EmulatorDetector()
    }

    @Test
    fun `detect returns empty list on legitimate hardware`() {
        setBuildFields(
            brand = "google",
            manufacturer = "Google",
            model = "Pixel 6",
            hardware = "tensor"
        )

        val result = detector.detect()
        assertTrue(result.isEmpty())
    }

    @Test
    fun `detect identifies emulator via build properties`() {
        // "generic" is a standard key in DetectorConst.EMULATOR_PROPS
        setBuildFields(brand = "emulator", device = "emulator")

        val result = detector.detect()

        assertEquals(1, result.size)
        val violation = result.first().violation as AndroidViolation.Emulator.Detected
        assertTrue(violation.name.orEmpty().contains("Prop:"))
    }

    @Test
    fun `isEmulator detection is case-insensitive`() {
        // Fix: Using "GOOGLE_SDK" because "google_sdk" is in your EMULATOR_PROPS
        setBuildFields(brand = "GOOGLE_SDK")

        val result = detector.detect()

        assertTrue("Should detect 'google_sdk' regardless of case", result.isNotEmpty())
    }

    @Test
    fun `detect identifies specific emulator brands`() {
        // Test for Genymotion
        setBuildFields(manufacturer = "Genymotion", hardware = "vbox86")

        val result = detector.detect()

        assertEquals(1, result.size)
        val violation = result.first().violation as AndroidViolation.Emulator.Detected
        assertTrue(violation.name.orEmpty().contains("vbox86") || violation.name.orEmpty().contains("genymotion"))
    }

    private fun setBuildFields(
        fingerprint: String = "unknown",
        device: String = "unknown",
        model: String = "unknown",
        brand: String = "unknown",
        product: String = "unknown",
        manufacturer: String = "unknown",
        hardware: String = "unknown",
    ) {
        ReflectionHelpers.setStaticField(Build::class.java, "FINGERPRINT", fingerprint)
        ReflectionHelpers.setStaticField(Build::class.java, "DEVICE", device)
        ReflectionHelpers.setStaticField(Build::class.java, "MODEL", model)
        ReflectionHelpers.setStaticField(Build::class.java, "BRAND", brand)
        ReflectionHelpers.setStaticField(Build::class.java, "PRODUCT", product)
        ReflectionHelpers.setStaticField(Build::class.java, "MANUFACTURER", manufacturer)
        ReflectionHelpers.setStaticField(Build::class.java, "HARDWARE", hardware)
    }
}