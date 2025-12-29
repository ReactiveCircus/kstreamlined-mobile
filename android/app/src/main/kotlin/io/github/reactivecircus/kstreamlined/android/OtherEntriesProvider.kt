package io.github.reactivecircus.kstreamlined.android

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import io.github.reactivecircus.kstreamlined.android.feature.contentviewer.ContentViewerScreen
import io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.KotlinWeeklyIssueScreen
import io.github.reactivecircus.kstreamlined.android.feature.licenses.api.LicensesRoute
import io.github.reactivecircus.kstreamlined.android.feature.settings.SettingsScreen
import io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.TalkingKotlinEpisodeScreen

fun EntryProviderScope<NavKey>.otherEntries(
    sharedTransitionScope: SharedTransitionScope,
    backStack: NavBackStack<NavKey>,
) = with(sharedTransitionScope) {
    entry<NavRoute.ContentViewer> {
        ContentViewerScreen(
            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
            boundsKey = it.boundsKey,
            topBarBoundsKey = it.topBarBoundsKey,
            saveButtonElementKey = it.saveButtonElementKey,
            id = it.id,
            onNavigateUp = {
                backStack.removeLastOrNull()
            },
        )
    }
    entry<NavRoute.KotlinWeeklyIssue> {
        KotlinWeeklyIssueScreen(
            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
            boundsKey = it.boundsKey,
            topBarBoundsKey = it.topBarBoundsKey,
            titleElementKey = it.titleElementKey,
            id = it.id,
            issueNumber = it.issueNumber,
            onNavigateUp = {
                backStack.removeLastOrNull()
            },
        )
    }
    entry<NavRoute.TalkingKotlinEpisode> {
        TalkingKotlinEpisodeScreen(
            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
            boundsKey = it.boundsKey,
            topBarBoundsKey = it.topBarBoundsKey,
            playerElementKey = it.playerElementKey,
            id = it.id,
            onNavigateUp = {
                backStack.removeLastOrNull()
            },
        )
    }
    entry<NavRoute.Settings> {
        SettingsScreen(
            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
            topBarBoundsKey = it.topBarBoundsKey,
            titleElementKey = it.titleElementKey,
            onOpenLicenses = {
                backStack.add(LicensesRoute)
            },
            onNavigateUp = {
                backStack.removeLastOrNull()
            },
        )
    }
}
