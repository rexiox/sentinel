package sentinel.benchmark

import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sentinel.Builder
import sentinel.Sentinel
import sentinel.all
import sentinel.configure
import sentinel.core.ext.toByteList
import sentinel.root
import sentinel.tamper
import sentinel.hook
import sentinel.emulator
import sentinel.debug
import sentinel.location

@RunWith(AndroidJUnit4::class)
class SentinelBenchmark {

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    private fun createSentinel(block: Builder.() -> Unit): Sentinel {
        return Sentinel.configure(context = context) {
            config {
                appId = Sentinel.Identity.appId.toByteList()
                appIntegrity = Sentinel.Identity.appIntegrity.toByteList()
                threshold = 20
            }
            block()
        }
    }

    private fun runBenchmark(configBlock: Builder.() -> Unit) {
        val sentinelInstance = createSentinel(configBlock)
        benchmarkRule.measureRepeated {
            runBlocking {
                sentinelInstance.inspect()
            }
        }
    }

    @Test
    fun benchmarkInspectRootOnly() = runBenchmark { root() }

    @Test
    fun benchmarkInspectTamperOnly() = runBenchmark { tamper() }

    @Test
    fun benchmarkInspectHookOnly() = runBenchmark { hook() }

    @Test
    fun benchmarkInspectEmulatorOnly() = runBenchmark { emulator() }

    @Test
    fun benchmarkInspectDebugOnly() = runBenchmark { debug() }

    @Test
    fun benchmarkInspectLocationOnly() = runBenchmark { location() }

    @Test
    fun benchmarkInspectAllModules() = runBenchmark { all() }

    @Test
    fun benchmarkColdStartAll() {
        benchmarkRule.measureRepeated {
            createSentinel { all() }
        }
    }

    @Test
    fun benchmarkColdStartRoot() {
        benchmarkRule.measureRepeated {
            createSentinel { root() }
        }
    }

    @Test
    fun benchmarkInspectWithWarmup() {
        val sentinelInstance = createSentinel { all() }

        repeat(5) {
            runBlocking { sentinelInstance.inspect() }
        }

        benchmarkRule.measureRepeated {
            runBlocking {
                sentinelInstance.inspect()
            }
        }
    }

    @Test
    fun benchmarkSequentialModules() {
        val sentinelInstance = createSentinel {
            root()
            tamper()
            hook()
        }

        benchmarkRule.measureRepeated {
            runBlocking {
                sentinelInstance.inspect()
            }
        }
    }

    @Test
    fun benchmarkMemoryAllocation() {
        benchmarkRule.measureRepeated {
            runBlocking {
                createSentinel { all() }.inspect()
            }
        }
    }
}