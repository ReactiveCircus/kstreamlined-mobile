package io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.kmp.feed.model.KotlinWeeklyIssueItem
import io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinweeklyissue.KotlinWeeklyIssueUiState
import org.junit.Rule
import org.junit.Test

@Burst
@Chameleon
class KotlinWeeklyIssueScreenTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_KotlinWeeklyIssueScreen_Loading() {
        snapshotTester.snapshot {
            KotlinWeeklyIssueScreenSnapshot(
                uiState = KotlinWeeklyIssueUiState.Loading,
            )
        }
    }

    @Test
    fun snapshot_KotlinWeeklyIssueScreen_Content() {
        snapshotTester.snapshot {
            KotlinWeeklyIssueScreenSnapshot(
                uiState = KotlinWeeklyIssueUiState.Content(
                    id = "1",
                    contentUrl = "content-url",
                    issueItems = mapOf(
                        KotlinWeeklyIssueItem.Group.Announcements to listOf(
                            KotlinWeeklyIssueItem(
                                title = "Apply for Google Summer of Code 2025 and Contribute to the Kotlin Ecosystem",
                                summary = "The Kotlin Foundation is once again participating in Google Summer of Code. If you are thinking of participating, check out this announcement.",
                                url = "url",
                                source = "blog.jetbrains.com",
                                group = KotlinWeeklyIssueItem.Group.Announcements,
                            ),
                        ),
                        KotlinWeeklyIssueItem.Group.Articles to listOf(
                            KotlinWeeklyIssueItem(
                                title = "Strong skipping does not fix Kotlin collections in Jetpack Compose",
                                summary = "Watch out when you are using List, Map, Set, etc in Compose. Jorge Castillo explains what strong skipping in Compose does not fix.",
                                url = "url",
                                source = "newsletter.jorgecastillo.dev",
                                group = KotlinWeeklyIssueItem.Group.Articles,
                            ),
                        ),
                        KotlinWeeklyIssueItem.Group.Videos to listOf(
                            KotlinWeeklyIssueItem(
                                title = "A full guide to MongoDB with Kotlin coroutines in Ktor",
                                summary = "Piotr from Codersee explains in this video how to integrate a Ktor application with the MongoDB database and Kotlin coroutines.",
                                url = "url",
                                source = "www.youtube.com",
                                group = KotlinWeeklyIssueItem.Group.Videos,
                            ),
                        ),
                    ),
                    savedForLater = true,
                ),
            )
        }
    }

    @Test
    fun snapshot_KotlinWeeklyIssueScreen_Error() {
        snapshotTester.snapshot {
            KotlinWeeklyIssueScreenSnapshot(
                uiState = KotlinWeeklyIssueUiState.Error,
            )
        }
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    @Composable
    fun KotlinWeeklyIssueScreenSnapshot(uiState: KotlinWeeklyIssueUiState) {
        SharedTransitionLayout {
            KotlinWeeklyIssueScreen(
                topBarBoundsKey = "",
                titleElementKey = "",
                id = "1",
                title = "Issue #448",
                onNavigateUp = {},
                onShareButtonClick = {},
                onOpenLink = {},
                uiState = uiState,
                eventSink = {},
            )
        }
    }
}
