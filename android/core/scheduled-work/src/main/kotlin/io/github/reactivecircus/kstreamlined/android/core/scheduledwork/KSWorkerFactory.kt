package io.github.reactivecircus.kstreamlined.android.core.scheduledwork

import androidx.work.DelegatingWorkerFactory
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import io.github.reactivecircus.kstreamlined.android.core.scheduledwork.sync.SyncWorker
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.FeedSyncEngine

@SingleIn(AppScope::class)
@Inject
public class KSWorkerFactory(
    feedSyncEngine: FeedSyncEngine,
) : DelegatingWorkerFactory() {
    init {
        addFactory(SyncWorker.Factory(feedSyncEngine))
    }
}
