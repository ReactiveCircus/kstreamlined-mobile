package io.github.reactivecircus.kstreamlined.android.benchmark.kotlinweeklyissue

import androidx.benchmark.macro.BaselineProfileMode
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.ExperimentalMetricApi
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.TraceSectionMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import io.github.reactivecircus.kstreamlined.android.benchmark.FeedOriginKey
import io.github.reactivecircus.kstreamlined.android.benchmark.PackageName
import io.github.reactivecircus.kstreamlined.android.benchmark.appState
import io.github.reactivecircus.kstreamlined.android.benchmark.home.CardType
import io.github.reactivecircus.kstreamlined.android.benchmark.home.clickCard
import io.github.reactivecircus.kstreamlined.android.benchmark.home.waitForHomeFeedContent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class KotlinWeeklyIssueBenchmark {
    @get:Rule
    val rule = MacrobenchmarkRule()

    @OptIn(ExperimentalMetricApi::class)
    @Test
    fun openAndCloseKotlinWeeklyIssueScreen() = rule.measureRepeated(
        packageName = PackageName,
        metrics = listOf(
            FrameTimingMetric(),
            TraceSectionMetric("HomeFeedList", mode = TraceSectionMetric.Mode.Sum),
            TraceSectionMetric("FeedItem:%Card", mode = TraceSectionMetric.Mode.Sum),
            TraceSectionMetric("Screen:%", mode = TraceSectionMetric.Mode.Sum),
        ),
        compilationMode = CompilationMode.Partial(BaselineProfileMode.Require),
        startupMode = StartupMode.WARM,
        iterations = 10,
        setupBlock = {
            pressHome()
            startActivityAndWait()
            waitForHomeFeedContent()
            appState { selectSingleFeed(FeedOriginKey.KotlinWeekly) }
        },
        measureBlock = {
            clickCard(CardType.KotlinWeekly)
            waitForKotlinWeeklyIssueContent()
            device.pressBack()
            pressHome()
        },
    )
}
