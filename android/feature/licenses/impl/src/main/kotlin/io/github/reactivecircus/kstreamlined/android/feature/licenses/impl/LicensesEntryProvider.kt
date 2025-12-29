package io.github.reactivecircus.kstreamlined.android.feature.licenses.impl

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import io.github.reactivecircus.kstreamlined.android.feature.licenses.api.LicensesRoute

public fun EntryProviderScope<NavKey>.licensesEntry(
    sharedTransitionScope: SharedTransitionScope,
    backStack: NavBackStack<NavKey>,
): Unit = with(sharedTransitionScope) {
    entry<LicensesRoute> {
        LicensesScreen(
            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
            onNavigateUp = {
                backStack.removeLastOrNull()
            },
        )
    }
}
