package io.github.reactivecircus.kstreamlined.android.foundation.commonui.feed

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.burst.Burst
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.ThemeVariant
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.toDisplayable
import org.junit.Rule
import org.junit.Test
import kotlin.time.Instant

@Burst
class KotlinWeeklyCardTest {

    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_KotlinWeeklyCard(
        saved: Boolean,
        themeVariant: ThemeVariant,
    ) {
        snapshotTester.snapshot(themeVariant = themeVariant) {
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
