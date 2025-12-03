package io.github.reactivecircus.kstreamlined.android.core.scheduledwork

import androidx.work.DelegatingWorkerFactory
import io.github.reactivecircus.kstreamlined.android.core.scheduledwork.sync.SyncWorker
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.FeedSyncEngine
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
public class KSWorkerFactory @Inject constructor(
    feedSyncEngine: FeedSyncEngine,
) : DelegatingWorkerFactory() {
    init {
        addFactory(SyncWorker.Factory(feedSyncEngine))
    }
}
