package io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.component

import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.ThemeVariantInjector
import io.github.reactivecircus.kstreamlined.kmp.model.feed.KotlinWeeklyIssueItem
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(ThemeVariantInjector::class)
class IssueItemUiTest {

    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_IssueItemUi() {
        snapshotTester.snapshot {
            IssueItemUi(
                item = KotlinWeeklyIssueItem(
                    title = "Amper Update – December 2023",
                    summary = "Last month JetBrains introduced Amper, a tool to improve the" +
                        " project configuration user experience. Marton Braun gives us an update" +
                        " about its state in December 2023.",
                    url = "https://blog.jetbrains.com/amper/2023/12/amper-update-december-2023/",
                    source = "blog.jetbrains.com",
                    group = KotlinWeeklyIssueItem.Group.Announcements,
                ),
                onItemClick = {},
            )
        }
    }
}
