package io.github.reactivecircus.kstreamlined.android

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope

fun EntryProviderScope<NavKey>.mainEntry(
    sharedTransitionScope: SharedTransitionScope,
    backStack: NavBackStack<NavKey>,
) = with(sharedTransitionScope) {
    entry<MainRoute> {
        MainScreen(
            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
            backStack = backStack,
        )
    }
}
