package io.github.reactivecircus.kstreamlined.android.feature.contentviewer.impl

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.TopNavBarSharedTransitionKeys
import io.github.reactivecircus.kstreamlined.android.feature.contentviewer.api.ContentViewerRoute
import io.github.reactivecircus.kstreamlined.android.feature.contentviewer.api.ContentViewerSharedTransitionKeys

public fun EntryProviderScope<NavKey>.contentViewerEntry(
    sharedTransitionScope: SharedTransitionScope,
    backStack: NavBackStack<NavKey>,
): Unit = with(sharedTransitionScope) {
    entry<ContentViewerRoute> {
        ContentViewerScreen(
            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
            boundsKey = ContentViewerSharedTransitionKeys.bounds(
                origin = it.origin,
                id = it.id,
            ),
            topBarBoundsKey = TopNavBarSharedTransitionKeys.bounds(it.origin),
            saveButtonElementKey = ContentViewerSharedTransitionKeys.saveButtonElement(
                origin = it.origin,
                id = it.id,
            ),
            id = it.id,
            onNavigateUp = {
                backStack.removeLastOrNull()
            },
        )
    }
}
