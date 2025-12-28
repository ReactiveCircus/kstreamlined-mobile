package io.github.reactivecircus.kstreamlined.android.core.ui.feed

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
class KotlinBlogCardTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_KotlinBlogCard(saved: Boolean) {
        snapshotTester.snapshot {
            SharedTransitionLayout {
                KotlinBlogCard(
                    item = FeedItem.KotlinBlog(
                        id = "1",
                        title = "Kotlin Multiplatform Development Roadmap for 2024",
                        publishTime = Instant.parse("2023-11-16T11:59:46Z"),
                        contentUrl = "contentUrl",
                        savedForLater = saved,
                        featuredImageUrl = "",
                    ).toDisplayable("Moments ago"),
                    onItemClick = {},
                    onSaveButtonClick = {},
                    modifier = Modifier.padding(24.dp),
                )
            }
        }
    }
}
