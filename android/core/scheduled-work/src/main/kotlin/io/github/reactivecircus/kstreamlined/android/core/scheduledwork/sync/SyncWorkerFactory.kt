package io.github.reactivecircus.kstreamlined.android.core.scheduledwork.sync

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import io.github.reactivecircus.kstreamlined.kmp.feed.sync.FeedSyncEngine

internal class SyncWorkerFactory(
    private val feedSyncEngine: FeedSyncEngine,
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters,
    ): ListenableWorker? {
        return when (workerClassName) {
            SyncWorker::class.java.name -> {
                SyncWorker(appContext, workerParameters, feedSyncEngine)
            }
            else -> null
        }
    }
}
