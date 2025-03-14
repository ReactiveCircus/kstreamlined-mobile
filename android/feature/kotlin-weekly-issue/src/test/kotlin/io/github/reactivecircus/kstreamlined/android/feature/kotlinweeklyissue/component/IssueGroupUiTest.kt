package io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.component

import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.ThemeVariant
import io.github.reactivecircus.kstreamlined.kmp.model.feed.KotlinWeeklyIssueItem
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(TestParameterInjector::class)
class IssueGroupUiTest {

    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_IssueGroupUi(
        @TestParameter group: KotlinWeeklyIssueItem.Group,
        @TestParameter themeVariant: ThemeVariant,
    ) {
        snapshotTester.snapshot(themeVariant = themeVariant) {
            IssueGroupUi(group = group)
        }
    }
}
