package io.github.reactivecircus.kstreamlined.kmp.remote

import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinBlogTldr
import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinWeeklyIssueEntry

public interface ContentService {
    public suspend fun fetchKotlinWeeklyIssue(url: String): List<KotlinWeeklyIssueEntry>

    public suspend fun fetchKotlinBlogTldr(id: String): KotlinBlogTldr
}
