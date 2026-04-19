package sentinel.kit.detector

import platform.Foundation.NSProcessInfo
import platform.UIKit.UIDevice
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.IosViolation
import sentinel.kit.detector.constant.DetectorConst

open class SimulatorDetector : SecurityDetector {

    override fun detect(): List<Threat> = buildList {
        if (isSimulator()) {
            add(element = Threat(violation = IosViolation.Simulator.Detected()))
        }
    }

    internal fun isSimulator(): Boolean {
        val model = UIDevice.currentDevice.model
        val env = NSProcessInfo.processInfo.environment
        val hasSimulatorEnv = DetectorConst.SIMULATOR_KEYS.any { key -> env[key] != null }
        val isSimulatorModel = DetectorConst.SIMULATOR_MODEL_KEYS.any { modelKey ->
            model.contains(other = modelKey, ignoreCase = true)
        }

        return isSimulatorModel || hasSimulatorEnv
    }
}