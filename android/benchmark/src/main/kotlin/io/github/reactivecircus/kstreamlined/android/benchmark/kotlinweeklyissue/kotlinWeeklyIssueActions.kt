package io.github.reactivecircus.kstreamlined.android.benchmark.kotlinweeklyissue

import androidx.benchmark.macro.MacrobenchmarkScope

fun MacrobenchmarkScope.waitForKotlinWeeklyIssueContent() {
    onElement { viewIdResourceName == "kotlinWeeklyIssue:content" }
}
