package io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.impl.component

import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.kmp.feed.model.KotlinWeeklyIssueItem
import org.junit.Rule
import org.junit.Test

@Burst
@Chameleon
class IssueGroupUiTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_IssueGroupUi(group: KotlinWeeklyIssueItem.Group) {
        snapshotTester.snapshot {
            IssueGroupUi(group = group)
        }
    }
}
