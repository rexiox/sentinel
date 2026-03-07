package sentinel.kit.detector

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSFileManager
import platform.Foundation.NSFileTypeSymbolicLink
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.IosViolation
import sentinel.kit.detector.constant.DetectorConst

class JailbreakDetector : SecurityDetector {

    override fun detect(): List<Threat> = buildList {
        if (checkJailbreakApps()) {
            add(
                element = Threat(
                    violation = IosViolation.Jailbreak.AppInstalled()
                )
            )
        }

        if (checkMounts()) {
            add(
                Threat(
                    violation = IosViolation.Jailbreak.SuspiciousMount()
                )
            )
        }
    }

    private fun checkJailbreakApps(): Boolean {
        val fileManager = NSFileManager.defaultManager

        return DetectorConst.JAILBREAK_APPS.any(predicate = fileManager::fileExistsAtPath)
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun checkMounts(): Boolean {
        val fileManager = NSFileManager.defaultManager

        for (path in DetectorConst.MOUNTS) {
            val attributes = try {
                fileManager.attributesOfItemAtPath(path, null)
            } catch (_: Throwable) {
                null
            }
            val type = attributes?.get("NSFileType") as? String
            if (type == NSFileTypeSymbolicLink) return true
        }

        return false
    }
}