package io.github.reactivecircus.kstreamlined.android.core.scheduledwork.sync

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.reactivecircus.kstreamlined.kmp.settings.datasource.SettingsDataSource
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

@OptIn(ExperimentalAtomicApi::class)
public class SyncScheduler @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val settingsDataSource: SettingsDataSource,
) {
    private val initialized = AtomicBoolean(false)

    public suspend fun initialize() {
        if (initialized.compareAndSet(expectedValue = false, newValue = true)) {
            settingsDataSource.appSettings.syncSettingsChanges().collectLatest {
                scheduleSync(it.enabled, it.syncInterval)
            }
        }
    }

    private fun scheduleSync(enabled: Boolean, syncInterval: Duration) {
        if (!enabled) {
            WorkManager.getInstance(context).cancelUniqueWork(WorkName)
            return
        }
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(syncInterval.toJavaDuration())
            .setInitialDelay(InitialDelay.toJavaDuration())
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WorkName,
            ExistingPeriodicWorkPolicy.UPDATE,
            syncWorkRequest,
        )
    }

    @Suppress("ConstPropertyName")
    private companion object {
        const val WorkName = "sync-feeds"
        val InitialDelay = 10.minutes
    }
}
