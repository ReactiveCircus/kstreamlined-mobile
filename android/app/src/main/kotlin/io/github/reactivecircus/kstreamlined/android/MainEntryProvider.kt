package io.github.reactivecircus.kstreamlined.android

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

context(entryProviderScope: EntryProviderScope<NavKey>, sharedTransitionScope: SharedTransitionScope)
fun mainEntry(
    backStack: NavBackStack<NavKey>,
) = entryProviderScope.entry<MainRoute> {
    sharedTransitionScope.MainScreen(
        backStack = backStack,
    )
}
