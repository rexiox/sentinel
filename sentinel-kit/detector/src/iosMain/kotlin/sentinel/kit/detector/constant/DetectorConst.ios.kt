package sentinel.kit.detector.constant

object DetectorConst {

    object SandBox {

        const val TEST_PATH = "/private/sentinel_jailbreak_test.txt"
        const val TEST_CONTENT = "sentinel_check"
    }

    val JB_APPS: List<String> = listOf(
        "/Applications/Cydia.app",
        "/Applications/Sileo.app",
        "/Applications/Zebra.app"
    )

    val JB_SYSTEM_PATHS: List<String> = listOf(
        "/Library/MobileSubstrate/MobileSubstrate.dylib",
        "/bin/bash",
        "/usr/sbin/sshd",
        "/etc/apt",
        "/private/var/lib/apt/",
        "/var/lib/cydia"
    )

    val SUSPICIOUS_SYMLINKS: List<String> = listOf(
        "/Applications",
        "/Library/Ringtones",
        "/Library/Wallpaper",
        "/usr/include",
        "/usr/libexec",
        "/usr/share"
    )

    val URL_SCHEMES: List<String> = listOf(
        "cydia://package/com.example.package",
        "cydia://",
        "undecimus://",
        "sileo://",
        "zbra://",
        "filza://",
    )

    val SIMULATOR_KEYS: List<String> = listOf(
        "SIMULATOR_DEVICE_NAME",
        "SIMULATOR_MODEL_IDENTIFIER"
    )

    val SIMULATOR_MODEL_KEYS: List<String> = listOf(
        "Simulator"
    )
}