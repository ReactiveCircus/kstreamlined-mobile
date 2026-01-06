package io.github.reactivecircus.kstreamlined.android.feature.settings.impl

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.TopNavBarSharedTransitionKeys
import io.github.reactivecircus.kstreamlined.android.feature.settings.api.SettingsRoute

public fun EntryProviderScope<NavKey>.settingsEntry(
    sharedTransitionScope: SharedTransitionScope,
    backStack: NavBackStack<NavKey>,
): Unit = with(sharedTransitionScope) {
    entry<SettingsRoute> {
        SettingsScreen(
            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
            backStack = backStack,
            topBarBoundsKey = TopNavBarSharedTransitionKeys.bounds(it.origin),
            titleElementKey = TopNavBarSharedTransitionKeys.titleElement(it.origin),
        )
    }
}
