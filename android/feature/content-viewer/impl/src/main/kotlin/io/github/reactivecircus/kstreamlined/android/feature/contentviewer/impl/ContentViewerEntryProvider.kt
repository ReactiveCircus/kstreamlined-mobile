package io.github.reactivecircus.kstreamlined.android.feature.contentviewer.impl

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.reactivecircus.kstreamlined.android.feature.contentviewer.api.ContentViewerRoute

context(entryProviderScope: EntryProviderScope<NavKey>, sharedTransitionScope: SharedTransitionScope)
public fun contentViewerEntry(
    backStack: NavBackStack<NavKey>,
): Unit = entryProviderScope.entry<ContentViewerRoute> {
    sharedTransitionScope.ContentViewerScreen(
        backStack = backStack,
        route = it,
    )
}
