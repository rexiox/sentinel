package sentinel.kit.detector

import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat

class TamperDetector constructor(
    appId: List<Byte>?,
    appSignature: List<Byte>?,
) : SecurityDetector {

    override fun detect(): List<Threat> = emptyList()
}