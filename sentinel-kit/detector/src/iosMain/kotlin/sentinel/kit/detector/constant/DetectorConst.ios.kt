package sentinel.kit.detector.constant

object DetectorConst {

    val SIMULATOR_KEYS: List<String> = listOf(
        "SIMULATOR_DEVICE_NAME",
        "SIMULATOR_MODEL_IDENTIFIER"
    )

    val SIMULATOR_MODEL_KEYS: List<String> = listOf(
        "Simulator"
    )

    val CRITICAL_SYSTEM_FUNCTIONS = listOf(
        "ptrace",
        "exit",
        "sysctl",
        "dlopen",
        "abort",
        "fork"
    )

    val URL_SCHEMES: List<String> = listOf(
        "cydia://package/com.example.package",
        "cydia://",
        "undecimus://",
        "sileo://",
        "zbra://",
        "filza://",
    )
}