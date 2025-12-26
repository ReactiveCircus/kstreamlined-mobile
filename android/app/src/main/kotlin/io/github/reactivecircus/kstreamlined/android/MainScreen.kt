@file:OptIn(ExperimentalSharedTransitionApi::class)

package io.github.reactivecircus.kstreamlined.android

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.NavigationIsland
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.NavigationIslandDivider
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.NavigationIslandItem
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.feature.home.HomeScreen
import io.github.reactivecircus.kstreamlined.android.feature.savedforlater.SavedForLaterScreen
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedItem
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun SharedTransitionScope.MainScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    selectedPage: MainNavRoute,
    onSelectPage: (MainNavRoute) -> Unit,
    homeListState: LazyListState,
    savedListState: LazyListState,
    onViewItem: (item: FeedItem, origin: MainNavRoute) -> Unit,
    onOpenSettings: (origin: MainNavRoute) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(
            initialPage = selectedPage.ordinal,
            pageCount = { MainNavRoute.entries.size },
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            beyondViewportPageCount = MainNavRoute.entries.size,
            userScrollEnabled = false,
        ) {
            when (it) {
                MainNavRoute.Home.ordinal -> {
                    HomeScreen(
                        animatedVisibilityScope = animatedVisibilityScope,
                        listState = homeListState,
                        onViewItem = { item -> onViewItem(item, MainNavRoute.Home) },
                        onOpenSettings = { onOpenSettings(MainNavRoute.Home) },
                        modifier = Modifier.pagerScaleTransition(it, pagerState),
                    )
                }

                MainNavRoute.Saved.ordinal -> {
                    SavedForLaterScreen(
                        animatedVisibilityScope = animatedVisibilityScope,
                        listState = savedListState,
                        onViewItem = { item -> onViewItem(item, MainNavRoute.Saved) },
                        onOpenSettings = { onOpenSettings(MainNavRoute.Saved) },
                        modifier = Modifier.pagerScaleTransition(it, pagerState),
                    )
                }
            }
        }

        LaunchedEffect(selectedPage) {
            pagerState.animateScrollToPage(
                page = selectedPage.ordinal,
                animationSpec = tween(
                    durationMillis = 400,
                    easing = EaseInOutQuart,
                ),
            )
        }

        val coroutineScope = rememberCoroutineScope()

        with(animatedVisibilityScope) {
            NavigationIsland(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(8.dp)
                    .align(Alignment.BottomCenter)
                    .renderInSharedTransitionScopeOverlay(
                        zIndexInOverlay = 1f,
                    )
                    .animateEnterExit(
                        enter = fadeIn() + slideInVertically(
                            tween(delayMillis = 200, easing = LinearOutSlowInEasing),
                        ) { it * 2 },
                        exit = fadeOut(),
                    ),
            ) {
                NavigationIslandItem(
                    selected = selectedPage == MainNavRoute.Home,
                    icon = KSIcons.Kotlin,
                    contentDescription = "Home",
                    onClick = {
                        if (pagerState.currentPage != MainNavRoute.Home.ordinal) {
                            onSelectPage(MainNavRoute.Home)
                        } else {
                            coroutineScope.launch {
                                homeListState.animateScrollToItem(0)
                            }
                        }
                    },
                )
                NavigationIslandDivider()
                NavigationIslandItem(
                    selected = selectedPage == MainNavRoute.Saved,
                    icon = KSIcons.Bookmarks,
                    contentDescription = "Saved",
                    onClick = {
                        if (pagerState.currentPage != MainNavRoute.Saved.ordinal) {
                            onSelectPage(MainNavRoute.Saved)
                        } else {
                            coroutineScope.launch {
                                savedListState.animateScrollToItem(0)
                            }
                        }
                    },
                )
            }
        }

        BackHandler(enabled = selectedPage != MainNavRoute.Home) {
            onSelectPage(MainNavRoute.Home)
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
