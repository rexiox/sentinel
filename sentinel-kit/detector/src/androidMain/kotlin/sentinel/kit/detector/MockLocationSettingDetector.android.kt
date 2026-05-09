package sentinel.kit.detector

import android.content.Context
import android.provider.Settings
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.AndroidViolation

class MockLocationSettingDetector(
    private val context: Context,
) : SecurityDetector {

    override fun detect(): List<Threat> = buildList {
        if (isMockLocationSettingEnabled()) {
            add(element = Threat(violation = AndroidViolation.Location.MockSettingEnabled))
        }
    }

    @Suppress("DEPRECATION")
    private fun isMockLocationSettingEnabled(): Boolean = Settings.Secure.getInt(
        context.contentResolver,
        Settings.Secure.ALLOW_MOCK_LOCATION,
        0
    ) != 0
}