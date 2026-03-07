package sentinel.kit.detector

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSFileTypeSymbolicLink
import platform.Foundation.NSURL
import platform.UIKit.UIApplication
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.IosViolation
import sentinel.kit.detector.constant.DetectorConst

class JailbreakDetector : SecurityDetector {

    override fun detect(): List<Threat> = buildList {
        if (checkJailbreakApps()) {
            add(element = Threat(violation = IosViolation.Jailbreak.AppInstalled()))
        }

        if (checkURLSchemes()) {
            add(element = Threat(violation = IosViolation.Jailbreak.URLSchemes()))
        }

        if (checkSuspiciousSymlinks()) {
            add(element = Threat(violation = IosViolation.Jailbreak.SuspiciousSymlinks()))
        }
    }

    private fun checkJailbreakApps(): Boolean {
        val fileManager = NSFileManager.defaultManager

        return DetectorConst.APP_PATHS.any(predicate = fileManager::fileExistsAtPath)
    }

    private fun checkURLSchemes(): Boolean {
        val sharedApp = UIApplication.sharedApplication

        return DetectorConst.URL_SCHEMES
            .mapNotNull { NSURL.URLWithString(it) }
            .any(predicate = sharedApp::canOpenURL)
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun checkSuspiciousSymlinks(): Boolean {
        val fileManager = NSFileManager.defaultManager

        return DetectorConst.SUSPICIOUS_SYMLINKS.any { path ->
            val attributes = fileManager.attributesOfItemAtPath(path, null)
            attributes?.get(platform.Foundation.NSFileType) == NSFileTypeSymbolicLink
        }
    }
}