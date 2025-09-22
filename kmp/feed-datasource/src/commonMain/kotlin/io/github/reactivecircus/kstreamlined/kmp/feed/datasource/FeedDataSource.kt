package io.github.reactivecircus.kstreamlined.kmp.feed.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import io.github.reactivecircus.kstreamlined.kmp.database.KStreamlinedDatabase
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.mapper.asExternalModel
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedOrigin
import io.github.reactivecircus.kstreamlined.kmp.model.feed.KotlinWeeklyIssueItem
import io.github.reactivecircus.kstreamlined.kmp.remote.FeedService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

public class FeedDataSource(
    private val feedService: FeedService,
    private val db: KStreamlinedDatabase,
    private val dbDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    public fun streamFeedOrigins(): Flow<List<FeedOrigin>> {
        return db.feedOriginEntityQueries.allFeedOrigins()
            .asFlow()
            .mapToList(dbDispatcher)
            .distinctUntilChanged()
            .map { origins ->
                origins.map { it.asExternalModel() }
            }
    }

    public fun streamFeedItemsForSelectedOrigins(): Flow<List<FeedItem>> {
        return db.feedItemEntityQueries.feedItemsForSelectedOrigins()
            .asFlow()
            .mapToList(dbDispatcher)
            .distinctUntilChanged()
            .map { items ->
                items.map { it.asExternalModel() }
            }
    }

    public fun streamSavedFeedItems(): Flow<List<FeedItem>> {
        return db.feedItemEntityQueries.savedFeedItems()
            .asFlow()
            .mapToList(dbDispatcher)
            .distinctUntilChanged()
            .map { items ->
                items.map { it.asExternalModel() }
            }
    }

    public fun streamFeedItemById(id: String): Flow<FeedItem?> {
        return db.feedItemEntityQueries.feedItemById(id)
            .asFlow()
            .mapToOneOrNull(dbDispatcher)
            .distinctUntilChanged()
            .map { it?.asExternalModel() }
    }

    public suspend fun loadKotlinWeeklyIssue(url: String): List<KotlinWeeklyIssueItem> {
        return feedService.fetchKotlinWeeklyIssue(url).map {
            it.asExternalModel()
        }
    }

    public suspend fun selectFeedSource(feedOriginKey: FeedOrigin.Key): Unit =
        withContext(dbDispatcher) {
            db.feedOriginEntityQueries.updateSelection(selected = true, key = feedOriginKey.name)
        }

    public suspend fun unselectFeedSource(feedOriginKey: FeedOrigin.Key): Unit =
        withContext(dbDispatcher) {
            db.transaction {
                // select all origins if the one being unselected is the only one selected
                val currentFeedOrigins = db.feedOriginEntityQueries.allFeedOrigins().executeAsList()
                val selectedOrigins = currentFeedOrigins.filter { it.selected }
                val isLastSelected = selectedOrigins.size == 1 &&
                    selectedOrigins.first().key == feedOriginKey.name
                if (isLastSelected) {
                    currentFeedOrigins.filter { it.key != feedOriginKey.name }.forEach { origin ->
                        db.feedOriginEntityQueries.updateSelection(
                            selected = true,
                            key = origin.key,
                        )
                    }
                }
                db.feedOriginEntityQueries.updateSelection(
                    selected = false,
                    key = feedOriginKey.name,
                )
            }
        }

    public suspend fun addSavedFeedItem(id: String): Unit = withContext(dbDispatcher) {
        db.feedItemEntityQueries.updateSavedForLaterById(saved_for_later = true, id = id)
    }

    public suspend fun removeSavedFeedItem(id: String): Unit = withContext(dbDispatcher) {
        db.feedItemEntityQueries.updateSavedForLaterById(saved_for_later = false, id = id)
    }

    public suspend fun saveTalkingKotlinEpisodeStartPosition(
        id: String,
        positionMillis: Long,
    ): Unit = withContext(dbDispatcher) {
        db.feedItemEntityQueries.updatePodcastStartPositionById(
            id = id,
            podcast_start_position = positionMillis,
        )
    }
}
