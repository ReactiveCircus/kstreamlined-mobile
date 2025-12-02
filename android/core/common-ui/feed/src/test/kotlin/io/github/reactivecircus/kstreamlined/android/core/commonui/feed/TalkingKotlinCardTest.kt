@file:OptIn(ExperimentalSharedTransitionApi::class)

package io.github.reactivecircus.kstreamlined.android.core.commonui.feed

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.feed.model.toDisplayable
import org.junit.Rule
import org.junit.Test
import kotlin.time.Instant

@Burst
@Chameleon
class TalkingKotlinCardTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_TalkingKotlinCard(saved: Boolean) {
        snapshotTester.snapshot {
            SharedTransitionLayout {
                TalkingKotlinCard(
                    item = FeedItem.TalkingKotlin(
                        id = "1",
                        title = "Making Multiplatform Better",
                        publishTime = Instant.parse("2023-09-18T22:00:00Z"),
                        contentUrl = "contentUrl",
                        savedForLater = saved,
                        audioUrl = "",
                        thumbnailUrl = "",
                        summary = "In this episode, we talk to Rick Clephas.",
                        summaryFormat = FeedItem.TalkingKotlin.ContentFormat.Text,
                        summaryPlainText = null,
                        duration = "1h 3min.",
                        startPositionMillis = 0,
                    ).toDisplayable("Moments ago"),
                    onItemClick = {},
                    onSaveButtonClick = {},
                    modifier = Modifier.padding(24.dp),
                )
            }
        }
    }
}
