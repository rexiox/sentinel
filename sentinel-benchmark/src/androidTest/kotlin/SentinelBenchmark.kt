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

    // INSPECT ONLY BENCHMARKS

    @Test
    fun benchmarkInspectRootOnly() {
        val sentinel = createSentinel { root() }

        benchmarkRule.measureRepeated {
            runBlocking {
                sentinel.inspect()
            }
        }
    }

    @Test
    fun benchmarkInspectTamperOnly() {
        val sentinel = createSentinel { tamper() }

        benchmarkRule.measureRepeated {
            runBlocking {
                sentinel.inspect()
            }
        }
    }

    @Test
    fun benchmarkInspectHookOnly() {
        val sentinel = createSentinel { hook() }

        benchmarkRule.measureRepeated {
            runBlocking {
                sentinel.inspect()
            }
        }
    }

    @Test
    fun benchmarkInspectEmulatorOnly() {
        val sentinel = createSentinel { emulator() }

        benchmarkRule.measureRepeated {
            runBlocking {
                sentinel.inspect()
            }
        }
    }

    @Test
    fun benchmarkInspectDebugOnly() {
        val sentinel = createSentinel { debug() }

        benchmarkRule.measureRepeated {
            runBlocking {
                sentinel.inspect()
            }
        }
    }

    @Test
    fun benchmarkInspectLocationOnly() {
        val sentinel = createSentinel { location() }

        benchmarkRule.measureRepeated {
            runBlocking {
                sentinel.inspect()
            }
        }
    }

    @Test
    fun benchmarkInspectAllModules() {
        val sentinel = createSentinel { all() }

        benchmarkRule.measureRepeated {
            runBlocking {
                sentinel.inspect()
            }
        }
    }

    @Test
    fun benchmarkInspectSequentialModules() {
        val sentinel = createSentinel {
            root()
            tamper()
            hook()
        }

        benchmarkRule.measureRepeated {
            runBlocking {
                sentinel.inspect()
            }
        }
    }

    // COLD START BENCHMARKS

    @Test
    fun benchmarkColdStartRoot() {
        benchmarkRule.measureRepeated {
            runBlocking {
                val sentinel = createSentinel { root() }
                sentinel.inspect()
            }
        }
    }

    @Test
    fun benchmarkColdStartTamper() {
        benchmarkRule.measureRepeated {
            runBlocking {
                val sentinel = createSentinel { tamper() }
                sentinel.inspect()
            }
        }
    }

    @Test
    fun benchmarkColdStartHook() {
        benchmarkRule.measureRepeated {
            runBlocking {
                val sentinel = createSentinel { hook() }
                sentinel.inspect()
            }
        }
    }

    @Test
    fun benchmarkColdStartEmulator() {
        benchmarkRule.measureRepeated {
            runBlocking {
                val sentinel = createSentinel { emulator() }
                sentinel.inspect()
            }
        }
    }

    @Test
    fun benchmarkColdStartDebug() {
        benchmarkRule.measureRepeated {
            runBlocking {
                val sentinel = createSentinel { debug() }
                sentinel.inspect()
            }
        }
    }

    @Test
    fun benchmarkColdStartLocation() {
        benchmarkRule.measureRepeated {
            runBlocking {
                val sentinel = createSentinel { location() }
                sentinel.inspect()
            }
        }
    }

    @Test
    fun benchmarkColdStartAll() {
        benchmarkRule.measureRepeated {
            runBlocking {
                val sentinel = createSentinel { all() }
                sentinel.inspect()
            }
        }
    }

    @Test
    fun benchmarkColdStartSequential() {
        benchmarkRule.measureRepeated {
            runBlocking {
                val sentinel = createSentinel {
                    root()
                    tamper()
                    hook()
                }
                sentinel.inspect()
            }
        }
    }

    // INITIALIZATION ONLY BENCHMARKS

    @Test
    fun benchmarkInitializationRoot() {
        benchmarkRule.measureRepeated {
            createSentinel { root() }
        }
    }

    @Test
    fun benchmarkInitializationTamper() {
        benchmarkRule.measureRepeated {
            createSentinel { tamper() }
        }
    }

    @Test
    fun benchmarkInitializationHook() {
        benchmarkRule.measureRepeated {
            createSentinel { hook() }
        }
    }

    @Test
    fun benchmarkInitializationEmulator() {
        benchmarkRule.measureRepeated {
            createSentinel { emulator() }
        }
    }

    @Test
    fun benchmarkInitializationDebug() {
        benchmarkRule.measureRepeated {
            createSentinel { debug() }
        }
    }

    @Test
    fun benchmarkInitializationLocation() {
        benchmarkRule.measureRepeated {
            createSentinel { location() }
        }
    }

    @Test
    fun benchmarkInitializationAll() {
        benchmarkRule.measureRepeated {
            createSentinel { all() }
        }
    }

    @Test
    fun benchmarkInitializationSequential() {
        benchmarkRule.measureRepeated {
            createSentinel {
                root()
                tamper()
                hook()
            }
        }
    }

    // WARMUP BENCHMARKS

    @Test
    fun benchmarkInspectWithManualWarmup() {
        val sentinel = createSentinel { all() }

        repeat(3) {
            runBlocking {
                sentinel.inspect()
            }
        }

        benchmarkRule.measureRepeated {
            runBlocking {
                sentinel.inspect()
            }
        }
    }

    // MEMORY PRESSURE BENCHMARKS

    @Test
    fun benchmarkMemoryAllocationSingle() {
        benchmarkRule.measureRepeated {
            runBlocking {
                val sentinel = createSentinel { all() }
                sentinel.inspect()
            }
        }
    }

    @Test
    fun benchmarkMemoryAllocationMultiple() {
        benchmarkRule.measureRepeated {
            runBlocking {
                val instances = mutableListOf<Sentinel>()

                repeat(5) {
                    instances.add(createSentinel { all() })
                }

                instances.forEach { it.inspect() }

                instances.clear()
            }
        }
    }

    @Test
    fun benchmarkMemoryPressureHeavy() {
        benchmarkRule.measureRepeated {
            runBlocking {
                val instances = mutableListOf<Sentinel>()

                repeat(10) { i ->
                    instances.add(
                        when (i % 6) {
                            0 -> createSentinel { root() }
                            1 -> createSentinel { tamper() }
                            2 -> createSentinel { hook() }
                            3 -> createSentinel { emulator() }
                            4 -> createSentinel { debug() }
                            else -> createSentinel { location() }
                        }
                    )
                }

                instances.forEach { it.inspect() }

                instances.clear()
            }
        }
    }

    // REUSE BENCHMARKS

    @Test
    fun benchmarkReuseInstance() {
        val sentinel = createSentinel { all() }

        benchmarkRule.measureRepeated {
            runBlocking {
                repeat(10) {
                    sentinel.inspect()
                }
            }
        }
    }

    @Test
    fun benchmarkReuseSingleInspect() {
        val sentinel = createSentinel { all() }

        benchmarkRule.measureRepeated {
            runBlocking {
                sentinel.inspect()
            }
        }
    }

    // EDGE CASE BENCHMARKS

    @Test
    fun benchmarkEmptyConfiguration() {
        benchmarkRule.measureRepeated {
            runBlocking {
                val sentinel = createSentinel { }
                sentinel.inspect()
            }
        }
    }

    @Test
    fun benchmarkAllModulesConcurrent() {
        val sentinel = createSentinel { all() }

        benchmarkRule.measureRepeated {
            runBlocking {
                sentinel.inspect()
                sentinel.inspect()
                sentinel.inspect()
            }
        }
    }
}