package io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.component

import app.cash.burst.Burst
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.ThemeVariant
import io.github.reactivecircus.kstreamlined.kmp.model.feed.KotlinWeeklyIssueItem
import org.junit.Rule
import org.junit.Test

@Burst
class IssueGroupUiTest {

    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_IssueGroupUi(
        group: KotlinWeeklyIssueItem.Group,
        themeVariant: ThemeVariant,
    ) {
        snapshotTester.snapshot(themeVariant = themeVariant) {
            IssueGroupUi(group = group)
        }
    }
}
