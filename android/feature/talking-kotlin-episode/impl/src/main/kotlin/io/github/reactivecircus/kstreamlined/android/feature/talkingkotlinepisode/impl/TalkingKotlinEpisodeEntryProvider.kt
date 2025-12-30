package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.impl

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.TopNavBarSharedTransitionKeys
import io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.api.TalkingKotlinEpisodeRoute
import io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.api.TalkingKotlinEpisodeSharedTransitionKeys

public fun EntryProviderScope<NavKey>.talkingKotlinEpisodeEntry(
    sharedTransitionScope: SharedTransitionScope,
    backStack: NavBackStack<NavKey>,
): Unit = with(sharedTransitionScope) {
    entry<TalkingKotlinEpisodeRoute> {
        TalkingKotlinEpisodeScreen(
            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
            boundsKey = TalkingKotlinEpisodeSharedTransitionKeys.bounds(
                origin = it.origin,
                id = it.id,
            ),
            topBarBoundsKey = TopNavBarSharedTransitionKeys.bounds(it.origin),
            playerElementKey = TalkingKotlinEpisodeSharedTransitionKeys.playerElement(
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
