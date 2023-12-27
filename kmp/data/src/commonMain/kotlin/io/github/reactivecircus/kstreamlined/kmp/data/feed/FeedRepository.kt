package io.github.reactivecircus.kstreamlined.kmp.data.feed

import co.touchlab.kermit.Logger
import io.github.reactivecircus.kstreamlined.kmp.data.feed.mapper.asExternalModel
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.FeedDataSource
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedOrigin
import io.github.reactivecircus.kstreamlined.kmp.model.feed.KotlinWeeklyIssueItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.datetime.toInstant

public class FeedRepository(
    private val feedDataSource: FeedDataSource
) {

    public val feedSyncState: Flow<FeedSyncState> = emptyFlow()

    public suspend fun syncNow() {
        feedDataSource.loadFeedEntries(null, true).also {
            Logger.i("<<Number of entries: ${it.size}>>")
            it.forEach { entry ->
                Logger.i("${entry::class.simpleName}: ${entry.title}, ${entry.publishTime}}")
            }
        }
    }

    public suspend fun selectFeedSource(feedOrigin: FeedOrigin) {
        feedOrigin.selected
        TODO()
    }

    public suspend fun unselectFeedSource(feedOrigin: FeedOrigin) {
        feedOrigin.selected
        TODO()
    }

    public suspend fun loadFeedItemById(id: String): FeedItem? {
        // TODO load from DB
        return FeedItem.KotlinWeekly(
            id = id,
            title = "Kotlin Weekly #386",
            publishTime = "2023-11-05T08:13:58Z".toInstant(),
            contentUrl = id,
            savedForLater = false,
            issueNumber = 386,
        )
    }

    public suspend fun loadSavedFeedItems(): List<FeedItem> {
        TODO()
    }

    public suspend fun loadKotlinWeeklyIssue(url: String): List<KotlinWeeklyIssueItem> {
        // TODO persist fetched entry to DB
        return feedDataSource.loadKotlinWeeklyIssue(url).map {
            it.asExternalModel()
        }
    }
}
