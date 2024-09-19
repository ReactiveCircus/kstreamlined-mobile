package io.github.reactivecircus.kstreamlined.android.benchmark.startup

import androidx.benchmark.ExperimentalBenchmarkConfigApi
import androidx.benchmark.ExperimentalConfig
import androidx.benchmark.StartupInsightsConfig
import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.benchmark.perfetto.ExperimentalPerfettoCaptureApi
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import io.github.reactivecircus.kstreamlined.android.benchmark.PackageName
import io.github.reactivecircus.kstreamlined.android.benchmark.home.waitForHomeFeedContent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class StartupBenchmark {

    @get:Rule
    val rule = MacrobenchmarkRule()

    @Test
    fun startupCompilationNone() =
        startup(CompilationMode.None())

    @Test
    fun startupCompilationBaselineProfiles() =
        startup(CompilationMode.Partial(BaselineProfileMode.Require))

    @OptIn(ExperimentalBenchmarkConfigApi::class, ExperimentalPerfettoCaptureApi::class)
    private fun startup(compilationMode: CompilationMode) = rule.measureRepeated(
        packageName = PackageName,
        metrics = listOf(StartupTimingMetric()),
        compilationMode = compilationMode,
        experimentalConfig = ExperimentalConfig(
            startupInsightsConfig = StartupInsightsConfig(isEnabled = true)
        ),
        startupMode = StartupMode.COLD,
        iterations = 10,
        setupBlock = {
            pressHome()
        },
        measureBlock = {
            startActivityAndWait()
            waitForHomeFeedContent()
        }
    )
}
