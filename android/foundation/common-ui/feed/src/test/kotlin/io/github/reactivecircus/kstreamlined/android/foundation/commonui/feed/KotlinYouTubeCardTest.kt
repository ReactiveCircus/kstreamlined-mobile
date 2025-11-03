@file:OptIn(ExperimentalSharedTransitionApi::class)

package io.github.reactivecircus.kstreamlined.android.foundation.commonui.feed

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.toDisplayable
import org.junit.Rule
import org.junit.Test
import kotlin.time.Instant

@Burst
@Chameleon
class KotlinYouTubeCardTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_KotlinYouTubeCard(saved: Boolean) {
        snapshotTester.snapshot {
            SharedTransitionLayout {
                KotlinYouTubeCard(
                    item = FeedItem.KotlinYouTube(
                        id = "1",
                        title = "The State of Kotlin Multiplatform",
                        publishTime = Instant.parse("2023-11-21T18:47:47Z"),
                        contentUrl = "contentUrl",
                        savedForLater = saved,
                        thumbnailUrl = "",
                        description = "JetBrains Kotlin Multiplatform (KMP) is an open-source technology",
                    ).toDisplayable("3 days ago"),
                    onItemClick = {},
                    onSaveButtonClick = {},
                    modifier = Modifier.padding(24.dp),
                )
            }
        }
    }
}
