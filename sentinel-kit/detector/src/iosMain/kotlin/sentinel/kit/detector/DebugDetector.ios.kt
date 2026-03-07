package sentinel.kit.detector

import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat

class DebugDetector : SecurityDetector {

    override fun detect(): List<Threat> = emptyList()
}