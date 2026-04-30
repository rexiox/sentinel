object Config {

    const val NAMESPACE = "co.rexiox.sentinel"

    object Version {

        const val MIN_SDK = 24
        const val COMPILE_SDK = 36
        const val NAME = "1.8.1"
    }

    object Publishing {

        const val GROUP_ID = "co.rexiox"
        const val URL = "https://github.com/rexiox/sentinel"

        object License {

            const val NAME = "MIT License"
            const val URL = "https://opensource.org/licenses/MIT"
        }

        object Developer {

            const val ID = "rexiox"
            const val NAME = "REXIOX"
            const val EMAIL = "r3x.lab@gmail.com"
        }

        object SCM {

            const val URL = "https://github.com/rexiox/sentinel"
            const val CONNECTION = "scm:git:git://github.com/rexiox/sentinel.git"
            const val DEV_CONNECTION = "scm:git:ssh://github.com/rexiox/sentinel.git"
        }
    }
}