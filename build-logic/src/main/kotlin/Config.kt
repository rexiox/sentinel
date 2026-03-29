object Config {

    const val NAMESPACE = "com.rs.sentinel"

    object Version {

        const val MIN_SDK = 24
        const val COMPILE_SDK = 36
        const val NAME = "1.3.0-alpha3"
    }

    object Publishing {

        const val GROUP_ID = "io.github.resulsilay"
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