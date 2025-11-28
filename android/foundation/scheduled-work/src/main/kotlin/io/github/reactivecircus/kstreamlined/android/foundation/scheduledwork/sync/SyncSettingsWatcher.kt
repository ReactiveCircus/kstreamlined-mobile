package io.github.reactivecircus.kstreamlined.android.foundation.scheduledwork.sync

import io.github.reactivecircus.kstreamlined.kmp.settings.model.AppSettings
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
internal fun Flow<AppSettings>.syncSettingsChanges(): Flow<SyncSettingsChangeEvent> =
    this.debounce(SettingsChangeDebounce)
        .distinctUntilChanged { old, new ->
            old.autoSync == new.autoSync && old.autoSyncInterval == new.autoSyncInterval
        }
        .map { SyncSettingsChangeEvent(it.autoSync, it.autoSyncInterval) }

internal data class SyncSettingsChangeEvent(val enabled: Boolean, val syncInterval: Duration)

internal val SettingsChangeDebounce = 500.milliseconds
