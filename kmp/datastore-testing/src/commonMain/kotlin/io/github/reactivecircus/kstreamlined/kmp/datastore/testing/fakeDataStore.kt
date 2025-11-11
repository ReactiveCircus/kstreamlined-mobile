@file:OptIn(ExperimentalAtomicApi::class)

package io.github.reactivecircus.kstreamlined.kmp.datastore.testing

import androidx.datastore.core.DataStore
import androidx.datastore.core.okio.OkioStorage
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesSerializer
import okio.Path.Companion.toPath
import okio.fakefilesystem.FakeFileSystem
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.fetchAndIncrement

public fun createFakeDataStore(): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(
        storage = OkioStorage(FakeFileSystem(), PreferencesSerializer) {
            "/test${storageFileCount.fetchAndIncrement()}.preferences_pb".toPath()
        },
    )
}

private val storageFileCount = AtomicInt(0)
