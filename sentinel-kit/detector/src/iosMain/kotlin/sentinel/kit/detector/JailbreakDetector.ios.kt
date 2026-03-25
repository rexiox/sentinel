package sentinel.kit.detector

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.create
import platform.Foundation.writeToFile
import platform.UIKit.UIApplication
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.IosViolation
import sentinel.kit.detector.constant.DetectorConst

class JailbreakDetector : SecurityDetector {

    override fun detect(): List<Threat> = buildList {
        if (checkSandbox()) {
            add(element = Threat(violation = IosViolation.Jailbreak.Sandbox()))
        }

        if (checkSuspiciousSymlinks()) {
            add(element = Threat(violation = IosViolation.Jailbreak.SuspiciousSymlinks()))
        }

        if (checkJailbreakApps()) {
            add(element = Threat(violation = IosViolation.Jailbreak.AppInstalled()))
        }

        if (checkURLSchemes()) {
            add(element = Threat(violation = IosViolation.Jailbreak.URLSchemes()))
        }
    }

    @OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
    private fun checkSandbox(): Boolean = runCatching {
        val path = DetectorConst.SandBox.TEST_PATH
        val content = platform.Foundation.NSString.create(
            string = DetectorConst.SandBox.TEST_CONTENT
        )

        val success = content.writeToFile(
            path = path,
            atomically = true,
            encoding = platform.Foundation.NSUTF8StringEncoding,
            error = null
        )

        when {
            success -> {
                NSFileManager.defaultManager.removeItemAtPath(path = path, error = null)
                true
            }

            else -> false
        }
    }.getOrDefault(defaultValue = false)

    @OptIn(ExperimentalForeignApi::class)
    private fun checkSuspiciousSymlinks(): Boolean {
        val fileManager = NSFileManager.defaultManager
        val hasSuspiciousFiles = DetectorConst.JB_SYSTEM_PATHS.any(
            predicate = fileManager::fileExistsAtPath
        )

        val hasSymlinks = DetectorConst.SUSPICIOUS_SYMLINKS.any { path ->
            fileManager.destinationOfSymbolicLinkAtPath(path = path, error = null) != null
        }

        return hasSuspiciousFiles || hasSymlinks
    }

    private fun checkJailbreakApps(): Boolean {
        return DetectorConst.JB_APPS.any(predicate = NSFileManager.defaultManager::fileExistsAtPath)
    }

    private fun checkURLSchemes(): Boolean {
        return DetectorConst.URL_SCHEMES
            .mapNotNull { url -> NSURL.URLWithString(URLString = url) }
            .any(predicate = UIApplication.sharedApplication::canOpenURL)
    }
}