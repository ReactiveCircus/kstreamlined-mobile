package io.github.reactivecircus.kstreamlined.android.benchmark.baselineprofile

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import io.github.reactivecircus.kstreamlined.android.benchmark.FeedOriginKey
import io.github.reactivecircus.kstreamlined.android.benchmark.PackageName
import io.github.reactivecircus.kstreamlined.android.benchmark.appState
import io.github.reactivecircus.kstreamlined.android.benchmark.contentviewer.waitForContentViewerContent
import io.github.reactivecircus.kstreamlined.android.benchmark.feedselection.toggleFeedOriginCard
import io.github.reactivecircus.kstreamlined.android.benchmark.home.CardType
import io.github.reactivecircus.kstreamlined.android.benchmark.home.clickCard
import io.github.reactivecircus.kstreamlined.android.benchmark.home.clickFilterChip
import io.github.reactivecircus.kstreamlined.android.benchmark.home.clickTldrButtonOnCard
import io.github.reactivecircus.kstreamlined.android.benchmark.home.homeFeedListScrollDown
import io.github.reactivecircus.kstreamlined.android.benchmark.home.waitForHomeFeedContent
import io.github.reactivecircus.kstreamlined.android.benchmark.kotlinweeklyissue.waitForKotlinWeeklyIssueContent
import io.github.reactivecircus.kstreamlined.android.benchmark.kotlinyoutubevideo.waitForKotlinYouTubeVideoContent
import io.github.reactivecircus.kstreamlined.android.benchmark.talkingkotlinepisode.waitForTalkingKotlinEpisodeContent
import io.github.reactivecircus.kstreamlined.android.benchmark.tldrsummary.waitForTldrSummaryContent
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
    @get:Rule
    val rule = BaselineProfileRule()

    @Test
    fun generate() = rule.collect(
        packageName = PackageName,
        includeInStartupProfile = true,
        strictStability = true,
    ) {
        pressHome()
        startActivityAndWait()
        waitForHomeFeedContent()

        // Kotlin Blog (Content Viewer) & TLDR Summary
        appState { selectSingleFeed(FeedOriginKey.KotlinBlog) }
        clickCard(CardType.KotlinBlog)
        waitForContentViewerContent()
        device.pressBack()
        clickTldrButtonOnCard()
        waitForTldrSummaryContent()
        device.pressBack()

        // Kotlin YouTube Video
        appState { selectSingleFeed(FeedOriginKey.KotlinYouTubeChannel) }
        clickCard(CardType.KotlinYouTube)
        waitForKotlinYouTubeVideoContent()
        device.pressBack()

        // Talking Kotlin Podcast
        appState { selectSingleFeed(FeedOriginKey.TalkingKotlinPodcast) }
        clickCard(CardType.TalkingKotlin)
        waitForTalkingKotlinEpisodeContent()
        device.pressBack()

        // Kotlin Weekly
        appState { selectSingleFeed(FeedOriginKey.KotlinWeekly) }
        clickCard(CardType.KotlinWeekly)
        waitForKotlinWeeklyIssueContent()
        device.pressBack()

        // Feed Selection
        appState { resetFeedSelections() }
        homeFeedListScrollDown()
        clickFilterChip()
        toggleFeedOriginCard(FeedOriginKey.KotlinWeekly)
        toggleFeedOriginCard(FeedOriginKey.KotlinWeekly)
        device.pressBack()
    }
}
