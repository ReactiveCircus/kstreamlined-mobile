package io.github.reactivecircus.kstreamlined.android.foundation.scheduledwork

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.reactivecircus.kstreamlined.android.foundation.scheduledwork.sync.SyncWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

public class WorkScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    public fun schedule() {
        scheduleSync()
    }

    private fun scheduleSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(SyncInterval)
            .setInitialDelay(InitialDelay.seconds, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            SyncWorker::class.simpleName!!,
            ExistingPeriodicWorkPolicy.KEEP,
            syncWorkRequest,
        )
    }

    private companion object {
        val SyncInterval = 6.hours.toJavaDuration()
        val InitialDelay = 10.minutes.toJavaDuration()
    }
}
