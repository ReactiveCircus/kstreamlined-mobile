package io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.impl

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.TopNavBarSharedTransitionKeys
import io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.api.KotlinWeeklyIssueRoute
import io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.api.KotlinWeeklyIssueSharedTransitionKeys

public fun EntryProviderScope<NavKey>.kotlinWeeklyIssueEntry(
    sharedTransitionScope: SharedTransitionScope,
    backStack: NavBackStack<NavKey>,
): Unit = with(sharedTransitionScope) {
    entry<KotlinWeeklyIssueRoute> {
        KotlinWeeklyIssueScreen(
            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
            boundsKey = KotlinWeeklyIssueSharedTransitionKeys.bounds(
                origin = it.origin,
                id = it.id,
            ),
            topBarBoundsKey = TopNavBarSharedTransitionKeys.bounds(it.origin),
            titleElementKey = TopNavBarSharedTransitionKeys.titleElement(it.origin),
            id = it.id,
            issueNumber = it.issueNumber,
            onNavigateUp = {
                backStack.removeLastOrNull()
            },
        )
    }
}
