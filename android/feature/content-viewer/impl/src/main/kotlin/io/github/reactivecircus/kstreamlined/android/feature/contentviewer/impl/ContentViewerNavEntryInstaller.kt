package io.github.reactivecircus.kstreamlined.android.feature.contentviewer.impl

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import io.github.reactivecircus.kstreamlined.android.core.navigation.NavEntryInstaller
import io.github.reactivecircus.kstreamlined.android.feature.contentviewer.api.ContentViewerRoute

@ContributesIntoSet(AppScope::class)
public class ContentViewerNavEntryInstaller : NavEntryInstaller {
    context(entryProviderScope: EntryProviderScope<NavKey>, sharedTransitionScope: SharedTransitionScope)
    override fun install(backStack: NavBackStack<NavKey>) {
        entryProviderScope.entry<ContentViewerRoute> {
            sharedTransitionScope.ContentViewerScreen(
                backStack = backStack,
                route = it,
            )
        }
    }
}
