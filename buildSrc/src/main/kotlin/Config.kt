object Config {

    const val NAMESPACE = "com.rs.sentinel"
    const val GROUP_ID = "io.github.resulsilay"
    const val IS_MINIFY_ENABLED = true

    object Version {

        const val MIN_SDK = 24
        const val TARGET_SDK = 36
        const val COMPILE_SDK = 36
        const val CODE = 2
        const val NAME = "1.0.0-alpha03"
    }

    object Publishing {

        const val ARTIFACT_ID = "sentinel"
        const val NAME = "Sentinel"
        const val DESCRIPTION = "Android Security Toolkit for protecting apps against tampering, reverse engineering, rooted devices, and insecure runtime environments."
        const val URL = "https://github.com/ResulSilay/Sentinel"

        object License {

            const val NAME = "MIT License"
            const val URL = "https://opensource.org/licenses/MIT"
        }

        object Developer {

            const val ID = "resulsilay"
            const val NAME = "Resul Silay"
            const val EMAIL = "resulsilay@gmail.com"
        }

        object SCM {

            const val URL = "https://github.com/ResulSilay/Sentinel"
            const val CONNECTION = "scm:git:git://github.com/ResulSilay/Sentinel.git"
            const val DEV_CONNECTION = "scm:git:ssh://github.com/ResulSilay/Sentinel.git"
        }
    }
}