package io.github.reactivecircus.kstreamlined.android.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.ThemeVariantInjector
import io.github.reactivecircus.kstreamlined.kmp.model.feed.DisplayableFeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.presentation.home.HomeFeedItem
import io.github.reactivecircus.kstreamlined.kmp.presentation.home.HomeUiState
import kotlinx.datetime.Instant
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(ThemeVariantInjector::class)
class HomeScreenTest {

    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_HomeScreen_Loading() {
        snapshotTester.snapshot {
            HomeScreenSnapshot(
                uiState = HomeUiState.Loading
            )
        }
    }

    @Test
    fun snapshot_HomeScreen_Content() {
        snapshotTester.snapshot {
            HomeScreenSnapshot(
                uiState = HomeUiState.Content(
                    selectedFeedCount = 3,
                    feedItems = listOf(
                        HomeFeedItem.SectionHeader("This week"),
                        HomeFeedItem.Item(
                            displayableFeedItem = DisplayableFeedItem(
                                FeedItem.KotlinYouTube(
                                    id = "1",
                                    title = "Coil Goes Multiplatform with Colin White | Talking Kotlin #127",
                                    publishTime = Instant.parse("2023-11-29T17:30:08Z"),
                                    contentUrl = "content-url",
                                    savedForLater = false,
                                    thumbnailUrl = "image-url",
                                    description = "Welcome to another engaging episode of Talking Kotlin! In this edition, we dive into the dynamic world of Android development with Colin White, the creator of the widely acclaimed Coil library. Join us as we discuss the latest developments, insights, and the exciting roadmap for Coil. \uD83D\uDE80 Highlights from this Episode: Learn about Colin's journey in developing the Coil library. Discover the pivotal role Coil plays in simplifying image loading for Android developers. Get an exclusive sneak peek into the upcoming Coil 3.0, featuring multi-platform support and seamless integration with Jetpack Compose. \uD83D\uDD17 Helpful Links: Coil Library GitHub: https://coil-kt.github.io/coil/ Follow Colin White on Twitter: https://twitter.com/colinwhi \uD83C\uDF10 Connect with the Kotlin Community: https://kotlinlang.org/community/ Kotlin Foundation: https://kotlinfoundation.org/ \uD83D\uDC49 Don't miss out on the latest insights and updates from the Kotlin world! Subscribe, hit the bell icon, and join the conversation in the comments below. \uD83D\uDCC8 Help us reach 20,000 views by liking, sharing, and subscribing! Your support keeps the Kotlin conversation alive.",
                                ),
                                displayablePublishTime = "Moments ago",
                            )
                        ),
                        HomeFeedItem.Item(
                            displayableFeedItem = DisplayableFeedItem(
                                FeedItem.KotlinWeekly(
                                    id = "2",
                                    title = "Kotlin Weekly #382",
                                    publishTime = Instant.parse("2023-11-29T12:30:08Z"),
                                    contentUrl = "content-url",
                                    savedForLater = false,
                                    issueNumber = 1,
                                ),
                                displayablePublishTime = "5 hours ago",
                            ),
                        ),
                        HomeFeedItem.Item(
                            displayableFeedItem = DisplayableFeedItem(
                                FeedItem.TalkingKotlin(
                                    id = "3",
                                    title = "Making Multiplatform Better",
                                    publishTime = Instant.parse("2023-11-23T17:00:38Z"),
                                    contentUrl = "content-url",
                                    savedForLater = true,
                                    audioUrl = "",
                                    thumbnailUrl = "",
                                    summary = "In this episode, we talk to Rick Clephas.",
                                    summaryFormat = FeedItem.TalkingKotlin.ContentFormat.Text,
                                    summaryPlainText = null,
                                    duration = "1h 3min.",
                                    startPositionMillis = 0,
                                ),
                                displayablePublishTime = "6 days ago",
                            )
                        )
                    ),
                    refreshing = false,
                    hasTransientError = false,
                )
            )
        }
    }

    @Test
    fun snapshot_HomeScreen_Error() {
        snapshotTester.snapshot {
            HomeScreenSnapshot(
                uiState = HomeUiState.Error,
            )
        }
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    fun HomeScreenSnapshot(uiState: HomeUiState) {
        SharedTransitionLayout {
            AnimatedVisibility(
                visible = true,
                enter = EnterTransition.None,
                exit = ExitTransition.None,
            ) {
                HomeScreen(
                    animatedVisibilityScope = this,
                    listState = rememberLazyListState(),
                    onViewItem = {},
                    uiState = uiState,
                    eventSink = {},
                )
            }
        }
    }
}
