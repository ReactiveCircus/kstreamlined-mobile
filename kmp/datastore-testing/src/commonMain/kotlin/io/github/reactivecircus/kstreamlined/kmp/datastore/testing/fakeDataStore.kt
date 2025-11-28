@file:OptIn(ExperimentalAtomicApi::class, ExperimentalCoroutinesApi::class)

package io.github.reactivecircus.kstreamlined.kmp.datastore.testing

import androidx.datastore.core.DataStore
import androidx.datastore.core.okio.OkioStorage
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesSerializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import okio.Path.Companion.toPath
import okio.fakefilesystem.FakeFileSystem
import kotlin.concurrent.atomics.AtomicInt
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.fetchAndIncrement

public fun createFakeDataStore(
    scope: CoroutineScope = TestScope(UnconfinedTestDispatcher()),
): DataStore<Preferences> {
    return PreferenceDataStoreFactory.create(
        storage = OkioStorage(FakeFileSystem(), PreferencesSerializer) {
            "/test${storageFileCount.fetchAndIncrement()}.preferences_pb".toPath()
        },
        scope = scope,
    )
}

private val storageFileCount = AtomicInt(0)
