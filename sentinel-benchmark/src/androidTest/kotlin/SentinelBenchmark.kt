import androidx.benchmark.junit4.BenchmarkRule
import androidx.benchmark.junit4.measureRepeated
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sentinel.Sentinel
import sentinel.all
import sentinel.configure
import sentinel.core.ext.toByteList

@RunWith(AndroidJUnit4::class)
class SentinelBenchmark {

    private lateinit var sentinel: Sentinel

    @get:Rule
    val benchmarkRule = BenchmarkRule()

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        sentinel = Sentinel.configure(context = context) {
            config {
                appId = Sentinel.Identity.appId.toByteList()
                appIntegrity = Sentinel.Identity.appIntegrity.toByteList()
                threshold = 20
            }

            all()
        }
    }

    @Test
    fun benchmarkInspect() {
        benchmarkRule.measureRepeated {
            runBlocking {
                sentinel.inspect()
            }
        }
    }
}