package io.github.reactivecircus.kstreamlined.android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.retainRetainedValuesStoreRegistry
import androidx.navigation3.runtime.NavEntryDecorator
import androidx.navigation3.runtime.NavKey

@Composable
internal fun rememberRetainedNavEntryDecorator(): NavEntryDecorator<NavKey> {
    val registry = retainRetainedValuesStoreRegistry()
    return remember(registry) {
        NavEntryDecorator(onPop = registry::clearChild) { entry ->
            registry.LocalRetainedValuesStoreProvider(entry.contentKey) {
                entry.Content()
            }
        }
    }
}
