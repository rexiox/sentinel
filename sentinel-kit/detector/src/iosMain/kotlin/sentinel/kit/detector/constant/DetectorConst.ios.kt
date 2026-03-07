package sentinel.kit.detector.constant

object DetectorConst {

    val APP_PATHS: List<String> = listOf(
        "/Applications/Cydia.app",
        "/Library/MobileSubstrate/MobileSubstrate.dylib",
        "/bin/bash",
        "/usr/sbin/sshd",
        "/etc/apt",
        "/private/var/lib/apt/",
        "/Applications/Sileo.app",
        "/Applications/Zebra.app",
        "/Applications/Filza.app",
        "/var/lib/cydia"
    )

    val SUSPICIOUS_SYMLINKS: List<String> = listOf(
        "/Library",
        "/usr/lib",
        "/bin",
        "/etc",
        "/var"
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