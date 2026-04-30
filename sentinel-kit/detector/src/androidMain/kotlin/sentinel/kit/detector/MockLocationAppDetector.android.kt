package sentinel.kit.detector

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import sentinel.core.detector.SecurityDetector
import sentinel.core.detector.Threat
import sentinel.core.violation.AndroidViolation
import sentinel.kit.detector.constant.DetectorConst

class MockLocationAppDetector(
    private val context: Context,
) : SecurityDetector {

    override fun detect(): List<Threat> {
        val mockLocationPackages = detectMockLocationPackages()

        return buildList {
            if (mockLocationPackages.isNotEmpty()) {
                add(
                    element = Threat(
                        violation = AndroidViolation.Location.MockAppInstalled(packages = mockLocationPackages)
                    )
                )
            }
        }
    }

    private fun detectMockLocationPackages(): List<String> {
        val packageManager = context.packageManager
        val packagesWithPermission = packageManager.getSafeInstalledPackages()
        val result = ArrayList<String>(8)

        for (i in 0 until packagesWithPermission.size) {
            val packageName = packagesWithPermission[i].packageName
            if (packageName != context.packageName) {
                result.add(packageName)
            }
        }

        val knownApps = DetectorConst.MOCK_LOCATION_PACKAGES.toList()
        for (i in 0 until knownApps.size) {
            val pName = knownApps[i]
            if (pName !in result) {
                try {
                    packageManager.getPackageInfo(pName, 0)
                    result.add(pName)
                } catch (_: Exception) { }
            }
        }

        return result
    }

    private fun PackageManager.getSafeInstalledPackages(): List<PackageInfo> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            getPackagesHoldingPermissions(
                arrayOf(ACCESS_MOCK_LOCATION_PERMISSION),
                PackageManager.PackageInfoFlags.of(0)
            )
        } else {
            getPackagesHoldingPermissions(arrayOf(ACCESS_MOCK_LOCATION_PERMISSION), 0)
        }
    }

    private companion object {

        const val ACCESS_MOCK_LOCATION_PERMISSION = "android.permission.ACCESS_MOCK_LOCATION"
    }
}