package io.github.reactivecircus.kstreamlined.android

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import io.github.reactivecircus.kstreamlined.android.core.navigation.NavEntryInstaller

@Inject
@ContributesIntoSet(AppScope::class)
class MainNavEntryInstaller : NavEntryInstaller {
    context(entryProviderScope: EntryProviderScope<NavKey>, sharedTransitionScope: SharedTransitionScope)
    override fun install(backStack: NavBackStack<NavKey>) {
        entryProviderScope.entry<MainRoute> {
            sharedTransitionScope.MainScreen(
                backStack = backStack,
            )
        }
    }
}
