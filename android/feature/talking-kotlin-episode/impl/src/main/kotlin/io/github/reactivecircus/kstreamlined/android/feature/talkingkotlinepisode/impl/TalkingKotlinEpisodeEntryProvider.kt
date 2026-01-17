package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.impl

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.api.TalkingKotlinEpisodeRoute

context(entryProviderScope: EntryProviderScope<NavKey>, sharedTransitionScope: SharedTransitionScope)
public fun talkingKotlinEpisodeEntry(
    backStack: NavBackStack<NavKey>,
): Unit = entryProviderScope.entry<TalkingKotlinEpisodeRoute> {
    sharedTransitionScope.TalkingKotlinEpisodeScreen(
        backStack = backStack,
        route = it,
    )
}
