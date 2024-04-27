@file:OptIn(ExperimentalSharedTransitionApi::class)

package io.github.reactivecircus.kstreamlined.android

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.EaseInOutQuart
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import io.github.reactivecircus.kstreamlined.android.feature.home.HomeScreen
import io.github.reactivecircus.kstreamlined.android.feature.savedforlater.SavedForLaterScreen
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.NavigationIsland
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.NavigationIslandDivider
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.NavigationIslandItem
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.Bookmarks
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.Kotlin
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import kotlin.math.absoluteValue

context(SharedTransitionScope, AnimatedVisibilityScope)
@Composable
fun MainScreen(
    selectedNavItem: NavItemKey,
    onSelectedNavItemChanged: (NavItemKey) -> Unit,
    homeListState: LazyListState,
    savedListState: LazyListState,
    onViewItem: (item: FeedItem, source: NavItemKey) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(
            initialPage = selectedNavItem.ordinal,
            pageCount = { NavItemKey.entries.size },
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            beyondViewportPageCount = NavItemKey.entries.size,
            userScrollEnabled = false,
        ) {
            when (it) {
                NavItemKey.Home.ordinal -> {
                    HomeScreen(
                        listState = homeListState,
                        onViewItem = { item -> onViewItem(item, NavItemKey.Home) },
                        modifier = Modifier.pagerScaleTransition(it, pagerState)
                    )
                }

                NavItemKey.Saved.ordinal -> {
                    SavedForLaterScreen(
                        listState = savedListState,
                        onViewItem = { item -> onViewItem(item, NavItemKey.Saved) },
                        modifier = Modifier.pagerScaleTransition(it, pagerState)
                    )
                }
            }
        }

        LaunchedEffect(selectedNavItem) {
            pagerState.animateScrollToPage(
                page = selectedNavItem.ordinal,
                animationSpec = tween(
                    durationMillis = 400,
                    easing = EaseInOutQuart,
                ),
            )
        }

        NavigationIsland(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(8.dp)
                .align(Alignment.BottomCenter)
                .renderInSharedTransitionScopeOverlay(
                    zIndexInOverlay = 1f
                )
                .animateEnterExit(
                    enter = fadeIn() + slideInVertically(
                        tween(delayMillis = 200, easing = LinearOutSlowInEasing)
                    ) { it * 2 },
                    exit = fadeOut(),
                ),
        ) {
            NavigationIslandItem(
                selected = selectedNavItem == NavItemKey.Home,
                icon = KSIcons.Kotlin,
                contentDescription = "Home",
                onClick = {
                    onSelectedNavItemChanged(NavItemKey.Home)
                },
            )
            NavigationIslandDivider()
            NavigationIslandItem(
                selected = selectedNavItem == NavItemKey.Saved,
                icon = KSIcons.Bookmarks,
                contentDescription = "Saved",
                onClick = {
                    onSelectedNavItemChanged(NavItemKey.Saved)
                },
            )
        }
    }
}

private fun Modifier.pagerScaleTransition(page: Int, pagerState: PagerState) = graphicsLayer {
    val pageOffset = (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
    lerp(
        start = 0.8f,
        stop = 1f,
        fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f),
    ).also { scale ->
        scaleX = scale
        scaleY = scale
    }
}

enum class NavItemKey {
    Home,
    Saved,
}
