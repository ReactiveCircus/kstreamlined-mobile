package io.github.reactivecircus.kstreamlined.android.core.commonui.feed

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
class KotlinWeeklyCardTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_KotlinWeeklyCard(saved: Boolean) {
        snapshotTester.snapshot {
            KotlinWeeklyCard(
                item = FeedItem.KotlinWeekly(
                    id = "1",
                    title = "Kotlin Weekly #381",
                    publishTime = Instant.parse("2023-11-19T09:13:00Z"),
                    contentUrl = "contentUrl",
                    savedForLater = saved,
                    issueNumber = 381,
                ).toDisplayable(displayablePublishTime = "3 hours ago"),
                onItemClick = {},
                onSaveButtonClick = {},
                modifier = Modifier.padding(24.dp),
            )
        }
    }
}
