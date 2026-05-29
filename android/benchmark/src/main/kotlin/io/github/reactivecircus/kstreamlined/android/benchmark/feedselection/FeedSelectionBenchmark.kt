package io.github.reactivecircus.kstreamlined.android.benchmark.feedselection

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
import io.github.reactivecircus.kstreamlined.android.benchmark.home.CardType
import io.github.reactivecircus.kstreamlined.android.benchmark.home.clickFilterChip
import io.github.reactivecircus.kstreamlined.android.benchmark.home.scrollToCard
import io.github.reactivecircus.kstreamlined.android.benchmark.home.waitForHomeFeedContent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class FeedSelectionBenchmark {
    @get:Rule
    val rule = MacrobenchmarkRule()

    @OptIn(ExperimentalMetricApi::class)
    @Test
    fun openAndCloseFeedSelectionBottomSheet() = rule.measureRepeated(
        packageName = PackageName,
        metrics = listOf(
            FrameTimingMetric(),
            TraceSectionMetric("Screen:FeedSelection", mode = TraceSectionMetric.Mode.Sum),
            TraceSectionMetric("FeedOrigin:%", mode = TraceSectionMetric.Mode.Sum),
        ),
        compilationMode = CompilationMode.Partial(BaselineProfileMode.Require),
        startupMode = StartupMode.WARM,
        iterations = 10,
        setupBlock = {
            pressHome()
            startActivityAndWait()
            waitForHomeFeedContent()
        },
        measureBlock = {
            clickFilterChip()
            device.pressBack()
            pressHome()
        },
    )

    @OptIn(ExperimentalMetricApi::class)
    @Test
    fun toggleSingleFeed() = rule.measureRepeated(
        packageName = PackageName,
        metrics = listOf(
            FrameTimingMetric(),
            TraceSectionMetric("Screen:FeedSelection", mode = TraceSectionMetric.Mode.Sum),
            TraceSectionMetric("FeedOrigin:%", mode = TraceSectionMetric.Mode.Sum),
        ),
        compilationMode = CompilationMode.Partial(BaselineProfileMode.Require),
        startupMode = StartupMode.WARM,
        iterations = 10,
        setupBlock = {
            pressHome()
            startActivityAndWait()
            waitForHomeFeedContent()
            clickFilterChip()
            waitForFeedSelectionContent()
        },
        measureBlock = {
            toggleFeedOriginCard(FeedOriginKey.KotlinBlog)
            toggleFeedOriginCard(FeedOriginKey.KotlinBlog)
        },
    )

    @OptIn(ExperimentalMetricApi::class)
    @Test
    fun deselectLastSelectedFeed() = rule.measureRepeated(
        packageName = PackageName,
        metrics = listOf(
            FrameTimingMetric(),
            TraceSectionMetric("Screen:FeedSelection", mode = TraceSectionMetric.Mode.Sum),
            TraceSectionMetric("FeedOrigin:%", mode = TraceSectionMetric.Mode.Sum),
            TraceSectionMetric("HomeFeedList", mode = TraceSectionMetric.Mode.Sum),
            TraceSectionMetric("FeedItem:%Card", mode = TraceSectionMetric.Mode.Sum),
        ),
        compilationMode = CompilationMode.Partial(BaselineProfileMode.Require),
        startupMode = StartupMode.WARM,
        iterations = 10,
        setupBlock = {
            pressHome()
            startActivityAndWait()
            waitForHomeFeedContent()
            clickFilterChip()
            waitForFeedSelectionContent()
        },
        measureBlock = {
            toggleFeedOriginCard(FeedOriginKey.KotlinBlog)
            toggleFeedOriginCard(FeedOriginKey.KotlinYouTubeChannel)
            toggleFeedOriginCard(FeedOriginKey.TalkingKotlinPodcast)
            toggleFeedOriginCard(FeedOriginKey.KotlinWeekly)
            dismissFeedSelectionSheet()
        },
    )

    @OptIn(ExperimentalMetricApi::class)
    @Test
    fun scrollHomeFeedAndDeselectFeedFromBottomSheet() = rule.measureRepeated(
        packageName = PackageName,
        metrics = listOf(
            FrameTimingMetric(),
            TraceSectionMetric("Screen:FeedSelection", mode = TraceSectionMetric.Mode.Sum),
            TraceSectionMetric("FeedOrigin:%", mode = TraceSectionMetric.Mode.Sum),
            TraceSectionMetric("HomeFeedList", mode = TraceSectionMetric.Mode.Sum),
            TraceSectionMetric("FeedItem:%Card", mode = TraceSectionMetric.Mode.Sum),
        ),
        compilationMode = CompilationMode.Partial(BaselineProfileMode.Require),
        startupMode = StartupMode.WARM,
        iterations = 10,
        setupBlock = {
            pressHome()
            startActivityAndWait()
            waitForHomeFeedContent()
            scrollToCard(CardType.KotlinWeekly)
            clickFilterChip()
            waitForFeedSelectionContent()
        },
        measureBlock = {
            toggleFeedOriginCard(FeedOriginKey.KotlinWeekly)
            dismissFeedSelectionSheet()
        },
    )
}
