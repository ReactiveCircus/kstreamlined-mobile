package io.github.reactivecircus.kstreamlined.android

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.layout.LazyLayoutCacheWindow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedItem

@OptIn(ExperimentalFoundationApi::class)
fun EntryProviderScope<NavKey>.mainEntry(
    sharedTransitionScope: SharedTransitionScope,
    backStack: NavBackStack<NavKey>,
) = with(sharedTransitionScope) {
    entry<MainRoute> {
        val dpCacheWindow = LazyLayoutCacheWindow(ahead = 300.dp, behind = 300.dp)
        val homeListState = rememberLazyListState(cacheWindow = dpCacheWindow)
        val savedListState = rememberLazyListState(cacheWindow = dpCacheWindow)
        var selectedPage by rememberSerializable { mutableStateOf(MainPagerItem.Home) }
        MainScreen(
            animatedVisibilityScope = LocalNavAnimatedContentScope.current,
            selectedPage = selectedPage,
            onSelectPage = { selectedPage = it },
            homeListState = homeListState,
            savedListState = savedListState,
            onViewItem = { item, origin ->
                backStack.add(
                    when (item) {
                        is FeedItem.KotlinWeekly -> {
                            NavRoute.KotlinWeeklyIssue(
                                boundsKey = "Bounds/$origin/${item.id}",
                                topBarBoundsKey = "Bounds/$origin/TopBar",
                                titleElementKey = "Element/$origin/TopBar/Title",
                                id = item.id,
                                issueNumber = item.issueNumber,
                            )
                        }

                        is FeedItem.TalkingKotlin -> {
                            NavRoute.TalkingKotlinEpisode(
                                boundsKey = "Bounds/$origin/${item.id}",
                                topBarBoundsKey = "Bounds/$origin/TopBar",
                                playerElementKey = "Element/$origin/${item.id}/player",
                                id = item.id,
                            )
                        }

                        else -> {
                            NavRoute.ContentViewer(
                                boundsKey = "Bounds/$origin/${item.id}",
                                topBarBoundsKey = "Bounds/$origin/TopBar",
                                saveButtonElementKey = "Element/$origin/${item.id}/saveButton",
                                id = item.id,
                            )
                        }
                    },
                )
            },
            onOpenSettings = { origin ->
                backStack.add(
                    NavRoute.Settings(
                        topBarBoundsKey = "Bounds/$origin/TopBar",
                        titleElementKey = "Element/$origin/TopBar/Title",
                    ),
                )
            },
        )
    }
}
