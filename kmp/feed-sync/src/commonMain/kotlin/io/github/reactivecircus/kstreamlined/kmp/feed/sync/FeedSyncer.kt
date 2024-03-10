package io.github.reactivecircus.kstreamlined.kmp.feed.sync

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import co.touchlab.kermit.Logger
import io.github.reactivecircus.kstreamlined.kmp.database.KStreamlinedDatabase
import io.github.reactivecircus.kstreamlined.kmp.networking.FeedService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

public interface FeedSyncer {
    public val syncState: StateFlow<SyncState>
    public suspend fun sync(forceRefresh: Boolean = false)
}

public class FeedSyncerImpl(
    private val feedService: FeedService,
    private val database: KStreamlinedDatabase,
    private val databaseDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : FeedSyncer {
    private val _syncState = MutableStateFlow(SyncState.Syncing)
    override val syncState: StateFlow<SyncState> = _syncState

    override suspend fun sync(forceRefresh: Boolean) {
        feedService.fetchFeedEntries(null, true).also {
            Logger.i("<<Number of entries: ${it.size}>>")
            it.forEach { entry ->
                Logger.i("${entry::class.simpleName}: ${entry.title}, ${entry.publishTime}}")
            }
        }
        database.feedIOriginEntityQueries.allFeedOrigins().asFlow().mapToOne(databaseDispatcher)
    }
}

public sealed interface SyncState {
    public data object Idle : SyncState
    public data object Syncing : SyncState
    public data object Error : SyncState
}
