package sentinel.identity.ext

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import java.security.MessageDigest

@SuppressLint("HardwareIds")
internal fun Context.getAndroidId(): String = Settings.Secure.getString(
    contentResolver,
    Settings.Secure.ANDROID_ID
).orEmpty()

internal fun Context.getAppSignatureSHA256(): String? {
    val packageInfo: PackageInfo = try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNING_CERTIFICATES
            )
        } else {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(
                packageName,
                PackageManager.GET_SIGNATURES
            )
        }
    } catch (e: PackageManager.NameNotFoundException) {
        throw RuntimeException("Package not found: $packageName", e)
    }

    val signatures = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packageInfo.signingInfo?.apkContentsSigners
    } else {
        @Suppress("DEPRECATION")
        packageInfo.signatures
    }

    if (signatures.isNullOrEmpty()) {
        return null
    }

    val cert = signatures[0]
    val md = MessageDigest.getInstance("SHA-256")
    val hash = md.digest(cert.toByteArray())

    return hash.joinToString(separator = "", transform = "%02X"::format)
}