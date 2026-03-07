package sentinel.kit.detector

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Debug.isDebuggerConnected
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.AndroidViolation
import sentinel.kit.detector.constant.DetectorConst

class DebugDetector(
    context: Context,
) : SecurityDetector {

    private val flags = context.applicationInfo.flags

    override fun detect(): List<Threat> {
        val isDebugger = isDebuggerConnected()
        val isDebuggable = (flags and ApplicationInfo.FLAG_DEBUGGABLE != 0)
        val isTestKeys = Build.TAGS?.contains(other = DetectorConst.TEST_KEYS_TAG) == true

        return buildList {
            if (isDebugger || isDebuggable) {
                add(
                    Threat(
                        violation = AndroidViolation.Debugger.Debuggable
                    )
                )
            }

            if (isTestKeys) {
                add(
                    Threat(
                        violation = AndroidViolation.Debugger.TestKeys
                    )
                )
            }
        }
    }
}