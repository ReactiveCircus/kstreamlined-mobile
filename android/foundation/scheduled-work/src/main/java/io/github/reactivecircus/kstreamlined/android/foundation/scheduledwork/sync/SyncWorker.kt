package io.github.reactivecircus.kstreamlined.android.foundation.scheduledwork.sync

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.tracing.traceAsync
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.FeedSyncEngine

@HiltWorker
internal class SyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val feedSyncEngine: FeedSyncEngine,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = traceAsync("ScheduledSync", 0) {
        feedSyncEngine.sync()
        Result.success()
    }
}
