package io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.impl

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.api.KotlinWeeklyIssueRoute

context(entryProviderScope: EntryProviderScope<NavKey>, sharedTransitionScope: SharedTransitionScope)
public fun kotlinWeeklyIssueEntry(
    backStack: NavBackStack<NavKey>,
): Unit = entryProviderScope.entry<KotlinWeeklyIssueRoute> {
    sharedTransitionScope.KotlinWeeklyIssueScreen(
        backStack = backStack,
        route = it,
    )
}
