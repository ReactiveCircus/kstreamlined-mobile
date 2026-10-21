package io.github.reactivecircus.kstreamlined.android.benchmark.screentransition

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
import io.github.reactivecircus.kstreamlined.android.benchmark.home.clickTldrButtonOnCard
import io.github.reactivecircus.kstreamlined.android.benchmark.home.waitForHomeFeedContent
import io.github.reactivecircus.kstreamlined.android.benchmark.home.waitForTldrSummaryContent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ScreenTransitionBenchmark {
    @get:Rule
    val rule = MacrobenchmarkRule()

    @Test
    fun openAndCloseDetailsScreenKotlinBlog() = openAndCloseDetailsScreen(CardType.KotlinBlog)

    @Test
    fun openAndCloseDetailsScreenKotlinYouTube() = openAndCloseDetailsScreen(CardType.KotlinYouTube)

    @Test
    fun openAndCloseDetailsScreenTalkingKotlin() = openAndCloseDetailsScreen(CardType.TalkingKotlin)

    @Test
    fun openAndCloseDetailsScreenKotlinWeekly() = openAndCloseDetailsScreen(CardType.KotlinWeekly)

    @Test
    fun openAndCloseTldrSummaryScreenCompilationNone() =
        openAndCloseTldrSummaryScreen(CompilationMode.None())

    @Test
    fun openAndCloseTldrSummaryScreenCompilationBaselineProfiles() =
        openAndCloseTldrSummaryScreen(CompilationMode.Partial(BaselineProfileMode.Require))

    @OptIn(ExperimentalMetricApi::class)
    private fun openAndCloseDetailsScreen(cardType: CardType) = rule.measureRepeated(
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
            appState { selectSingleFeed(cardType.toFeedOriginKey()) }
            waitForHomeFeedContent()
        },
        measureBlock = {
            clickCard(cardType)
            device.pressBack()
            pressHome()
        },
    )

    @OptIn(ExperimentalMetricApi::class)
    private fun openAndCloseTldrSummaryScreen(compilationMode: CompilationMode) = rule.measureRepeated(
        packageName = PackageName,
        metrics = listOf(
            FrameTimingMetric(),
            TraceSectionMetric("HomeFeedList", mode = TraceSectionMetric.Mode.Sum),
            TraceSectionMetric("FeedItem:%Card", mode = TraceSectionMetric.Mode.Sum),
            TraceSectionMetric("Screen:%", mode = TraceSectionMetric.Mode.Sum),
            TraceSectionMetric("Markdown:parse", mode = TraceSectionMetric.Mode.Sum),
        ),
        compilationMode = compilationMode,
        startupMode = StartupMode.WARM,
        iterations = 10,
        setupBlock = {
            pressHome()
            startActivityAndWait()
            appState { selectSingleFeed(FeedOriginKey.KotlinBlog) }
            waitForHomeFeedContent()
        },
        measureBlock = {
            clickTldrButtonOnCard()
            waitForTldrSummaryContent()
            device.pressBack()
            pressHome()
        },
    )
}
