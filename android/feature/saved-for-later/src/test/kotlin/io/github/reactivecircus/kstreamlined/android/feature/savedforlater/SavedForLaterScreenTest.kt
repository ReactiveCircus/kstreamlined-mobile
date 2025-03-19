package io.github.reactivecircus.kstreamlined.android.feature.savedforlater

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
import io.github.reactivecircus.kstreamlined.kmp.presentation.savedforlater.SavedForLaterUiState
import kotlinx.datetime.Instant
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(ThemeVariantInjector::class)
class SavedForLaterScreenTest {

    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_SavedForLaterScreen_Content() {
        snapshotTester.snapshot {
            SavedForLaterScreenSnapshot(
                uiState = SavedForLaterUiState.Content(
                    feedItems = listOf(
                        DisplayableFeedItem(
                            FeedItem.KotlinBlog(
                                id = "1",
                                title = "Kotlin Multiplatform Development Roadmap for 2024",
                                publishTime = Instant.parse("2023-11-30T11:59:46Z"),
                                contentUrl = "contentUrl",
                                savedForLater = true,
                                featuredImageUrl = "",
                            ),
                            displayablePublishTime = "30 Nov 2023",
                        ),
                        DisplayableFeedItem(
                            FeedItem.KotlinWeekly(
                                id = "2",
                                title = "Kotlin Weekly #382",
                                publishTime = Instant.parse("2023-11-29T12:30:08Z"),
                                contentUrl = "content-url",
                                savedForLater = true,
                                issueNumber = 1,
                            ),
                            displayablePublishTime = "29 Nov 2023",
                        ),
                        DisplayableFeedItem(
                            FeedItem.TalkingKotlin(
                                id = "3",
                                title = "Making Multiplatform Better",
                                publishTime = Instant.parse("2023-11-23T17:00:38Z"),
                                contentUrl = "content-url",
                                savedForLater = true,
                                audioUrl = "",
                                thumbnailUrl = "",
                                summary = "In this episode, we talk to Rick Clephas.",
                                duration = "1h 3min.",
                                startPositionMillis = 0,
                            ),
                            displayablePublishTime = "23 Nov 2023",
                        ),
                        DisplayableFeedItem(
                            FeedItem.KotlinYouTube(
                                id = "4",
                                title = "The State of Kotlin Multiplatform",
                                publishTime = Instant.parse("2023-11-21T18:47:47Z"),
                                contentUrl = "content-url",
                                savedForLater = true,
                                thumbnailUrl = "",
                                description = "JetBrains Kotlin Multiplatform (KMP) is an open-source technology",
                            ),
                            displayablePublishTime = "21 Nov 2023",
                        )
                    ),
                )
            )
        }
    }

    @Test
    fun snapshot_SavedForLaterScreen_Empty() {
        snapshotTester.snapshot {
            SavedForLaterScreenSnapshot(
                uiState = SavedForLaterUiState.Content(emptyList()),
            )
        }
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    fun SavedForLaterScreenSnapshot(uiState: SavedForLaterUiState) {
        SharedTransitionLayout {
            AnimatedVisibility(
                visible = true,
                enter = EnterTransition.None,
                exit = ExitTransition.None,
            ) {
                SavedForLaterScreen(
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
