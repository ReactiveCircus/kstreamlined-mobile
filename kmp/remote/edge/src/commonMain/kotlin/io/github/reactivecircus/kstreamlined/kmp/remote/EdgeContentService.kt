package io.github.reactivecircus.kstreamlined.kmp.remote

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinWeeklyIssueEntry

@ContributesBinding(AppScope::class)
internal class EdgeContentService : ContentService {
    override suspend fun fetchKotlinWeeklyIssue(url: String): List<KotlinWeeklyIssueEntry> {
        TODO()
    }
}
