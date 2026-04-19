@file:OptIn(ExperimentalForeignApi::class)

package sentinel.kit.detector

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.IosViolation
import sentinel.detector.checkJailbreakApps
import sentinel.detector.checkSandbox
import sentinel.detector.checkSuspiciousSymlinks
import sentinel.detector.checkSystemPaths
import sentinel.kit.detector.constant.DetectorConst

open class JailbreakDetector : SecurityDetector {

    override fun detect(): List<Threat> = buildList {
        if (checkSandbox()) {
            add(element = Threat(violation = IosViolation.Jailbreak.Sandbox()))
        }

        if (checkSystemPaths()) {
            add(element = Threat(violation = IosViolation.Jailbreak.SystemPaths()))
        }

        if (checkSuspiciousSymlinks()) {
            add(element = Threat(violation = IosViolation.Jailbreak.SuspiciousSymlinks()))
        }

        if (checkJailbreakApps()) {
            add(element = Threat(violation = IosViolation.Jailbreak.AppInstalled()))
        }

        if (checkUrlSchemes()) {
            add(element = Threat(violation = IosViolation.Jailbreak.URLSchemes()))
        }
    }

    private fun checkUrlSchemes(): Boolean = DetectorConst.URL_SCHEMES
        .mapNotNull { url -> NSURL.URLWithString(URLString = url) }
        .any(predicate = UIApplication.sharedApplication::canOpenURL)
}