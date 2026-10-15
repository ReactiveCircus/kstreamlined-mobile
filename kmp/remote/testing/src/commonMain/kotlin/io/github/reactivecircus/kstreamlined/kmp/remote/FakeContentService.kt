package io.github.reactivecircus.kstreamlined.kmp.remote

import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinWeeklyIssueEntry

public class FakeContentService : ContentService {
    public var nextKotlinWeeklyIssueResponse: suspend (
        url: String,
    ) -> List<KotlinWeeklyIssueEntry> = {
        FakeKotlinWeeklyIssueEntries
    }

    override suspend fun fetchKotlinWeeklyIssue(url: String): List<KotlinWeeklyIssueEntry> {
        return nextKotlinWeeklyIssueResponse(url)
    }
}
