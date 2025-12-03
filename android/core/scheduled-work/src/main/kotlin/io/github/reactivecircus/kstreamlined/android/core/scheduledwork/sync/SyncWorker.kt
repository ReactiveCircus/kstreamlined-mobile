package io.github.reactivecircus.kstreamlined.android.core.scheduledwork.sync

import android.content.Context
import androidx.tracing.traceAsync
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.FeedSyncEngine

internal class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val feedSyncEngine: FeedSyncEngine,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result = traceAsync("ScheduledSync", 0) {
        feedSyncEngine.sync()
        Result.success()
    }

    class Factory(
        private val feedSyncEngine: FeedSyncEngine,
    ) : WorkerFactory() {
        override fun createWorker(
            appContext: Context,
            workerClassName: String,
            workerParameters: WorkerParameters,
        ): ListenableWorker {
            return SyncWorker(appContext, workerParameters, feedSyncEngine)
        }
    }
}
