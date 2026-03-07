package sentinel

import sentinel.core.detector.SecurityDetector

expect class Builder {

    fun config(block: Config.() -> Unit): Config

    fun addDetector(detector: SecurityDetector) : Builder

    fun build(): Sentinel
}