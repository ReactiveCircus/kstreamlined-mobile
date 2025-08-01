package io.github.reactivecircus.kstreamlined.android.foundation.commonui.feed

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.ThemeVariant
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.toDisplayable
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.time.Instant

@RunWith(TestParameterInjector::class)
class KotlinWeeklyCardTest {

    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_KotlinWeeklyCard(
        @TestParameter saved: Boolean,
        @TestParameter themeVariant: ThemeVariant,
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
