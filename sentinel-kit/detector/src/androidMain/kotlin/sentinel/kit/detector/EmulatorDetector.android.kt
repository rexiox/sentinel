package sentinel.kit.detector

import android.os.Build
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.AndroidViolation
import sentinel.kit.detector.constant.DetectorConst
import java.io.File

class EmulatorDetector : SecurityDetector {

    override fun detect(): List<Threat> {
        val (pipe, prop) = isEmulator()

        return buildList {
            if (pipe != null) {
                add(
                    element = Threat(
                        violation = AndroidViolation.Emulator.Detected(name = "Pipe: $pipe")
                    )
                )
            }

            if (prop != null) {
                add(
                    element = Threat(
                        violation = AndroidViolation.Emulator.Detected(name = "Prop: $prop")
                    )
                )
            }
        }
    }

    private fun isEmulator(): Pair<String?, String?> {
        val buildDetails = (
                Build.FINGERPRINT
                        + Build.DEVICE
                        + Build.MODEL
                        + Build.BRAND
                        + Build.PRODUCT
                        + Build.MANUFACTURER
                        + Build.HARDWARE
                ).lowercase()

        val pipe = DetectorConst.EMULATOR_PIPES.firstOrNull { File(it).exists() }
        val prop = DetectorConst.EMULATOR_PROPS.firstOrNull { buildDetails.contains(it) }
        return Pair(pipe, prop)
    }
}