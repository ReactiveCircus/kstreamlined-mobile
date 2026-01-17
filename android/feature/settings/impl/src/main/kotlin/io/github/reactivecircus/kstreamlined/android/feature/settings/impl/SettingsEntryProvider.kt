package io.github.reactivecircus.kstreamlined.android.feature.settings.impl

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.reactivecircus.kstreamlined.android.feature.settings.api.SettingsRoute

context(entryProviderScope: EntryProviderScope<NavKey>, sharedTransitionScope: SharedTransitionScope)
public fun settingsEntry(
    backStack: NavBackStack<NavKey>,
): Unit = entryProviderScope.entry<SettingsRoute> {
    sharedTransitionScope.SettingsScreen(
        backStack = backStack,
        route = it,
    )
}
