package sentinel

import sentinel.core.report.SecurityReport
import sentinel.runtime.InstallScope

expect class Sentinel {

    val config: Config

    fun runtime(block: InstallScope.() -> Unit)

    suspend fun inspect(): SecurityReport
}

