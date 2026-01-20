package io.github.reactivecircus.kstreamlined.android.feature.settings.impl

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import io.github.reactivecircus.kstreamlined.android.core.navigation.NavEntryInstaller
import io.github.reactivecircus.kstreamlined.android.feature.settings.api.SettingsRoute

@ContributesIntoSet(AppScope::class)
public class SettingsNavEntryInstaller : NavEntryInstaller {
    context(entryProviderScope: EntryProviderScope<NavKey>, sharedTransitionScope: SharedTransitionScope)
    override fun install(backStack: NavBackStack<NavKey>) {
        entryProviderScope.entry<SettingsRoute> {
            sharedTransitionScope.SettingsScreen(
                backStack = backStack,
                route = it,
            )
        }
    }
}
