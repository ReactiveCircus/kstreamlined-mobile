package io.github.reactivecircus.kstreamlined.android.benchmark.home

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.TraceSectionMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import io.github.reactivecircus.kstreamlined.android.benchmark.PackageName
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class HomeFeedScrollingBenchmark {
    @get:Rule
    val rule = MacrobenchmarkRule()

    @Test
    fun scrollHomeFeedCompilationNone() =
        scrollHomeFeed(CompilationMode.None())

    @Test
    fun scrollHomeFeedCompilationBaselineProfiles() =
        scrollHomeFeed(CompilationMode.Partial(BaselineProfileMode.Require))

    @OptIn(ExperimentalMetricApi::class)
    private fun scrollHomeFeed(compilationMode: CompilationMode) = rule.measureRepeated(
        packageName = PackageName,
        metrics = listOf(
            FrameTimingMetric(),
            TraceSectionMetric("FeedItem:%Card", mode = TraceSectionMetric.Mode.Sum),
        ),
        compilationMode = compilationMode,
        startupMode = StartupMode.WARM,
        iterations = 10,
        setupBlock = {
            pressHome()
            startActivityAndWait()
            waitForHomeFeedContent()
        },
        measureBlock = {
            homeFeedListFlingDownUp()
        },
    )
}
