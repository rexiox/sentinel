package sentinel.identity

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.provider.Settings
import android.content.pm.PackageManager
import android.os.Build
import sentinel.core.identity.Identity
import java.security.MessageDigest

actual class Identity actual constructor(
    private val context: Any?,
) : Identity {

    private val androidContext: Context
        get() = context as? Context
            ?: throw IllegalArgumentException("Identity requires a valid Android Context")

    @SuppressLint("HardwareIds")
    actual override val deviceId: String = Settings.Secure.getString(
        androidContext.contentResolver,
        Settings.Secure.ANDROID_ID
    )

    actual override val appId: String = androidContext.packageName

    actual override val appIntegrity: String? = androidContext.getAppSignatureSHA256()

    actual override val platform: String = "Android"

    private fun Context.getAppSignatureSHA256(): String? {
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
}