package io.github.reactivecircus.kstreamlined.android

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
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
    selectedPage: MainPagerItem,
    onSelectPage: (MainPagerItem) -> Unit,
    homeListState: LazyListState,
    savedListState: LazyListState,
    onViewItem: (item: FeedItem, origin: MainPagerItem) -> Unit,
    onOpenSettings: (origin: MainPagerItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        val pagerState = rememberPagerState(
            initialPage = selectedPage.ordinal,
            pageCount = { MainPagerItem.entries.size },
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            beyondViewportPageCount = MainPagerItem.entries.size,
            userScrollEnabled = false,
        ) {
            when (it) {
                MainPagerItem.Home.ordinal -> {
                    HomeScreen(
                        animatedVisibilityScope = animatedVisibilityScope,
                        listState = homeListState,
                        onViewItem = { item -> onViewItem(item, MainPagerItem.Home) },
                        onOpenSettings = { onOpenSettings(MainPagerItem.Home) },
                        modifier = Modifier.pagerScaleTransition(it, pagerState),
                    )
                }

                MainPagerItem.Saved.ordinal -> {
                    SavedForLaterScreen(
                        animatedVisibilityScope = animatedVisibilityScope,
                        listState = savedListState,
                        onViewItem = { item -> onViewItem(item, MainPagerItem.Saved) },
                        onOpenSettings = { onOpenSettings(MainPagerItem.Saved) },
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
                    selected = selectedPage == MainPagerItem.Home,
                    icon = KSIcons.Kotlin,
                    contentDescription = "Home",
                    onClick = {
                        if (pagerState.currentPage != MainPagerItem.Home.ordinal) {
                            onSelectPage(MainPagerItem.Home)
                        } else {
                            coroutineScope.launch {
                                homeListState.animateScrollToItem(0)
                            }
                        }
                    },
                )
                NavigationIslandDivider()
                NavigationIslandItem(
                    selected = selectedPage == MainPagerItem.Saved,
                    icon = KSIcons.Bookmarks,
                    contentDescription = "Saved",
                    onClick = {
                        if (pagerState.currentPage != MainPagerItem.Saved.ordinal) {
                            onSelectPage(MainPagerItem.Saved)
                        } else {
                            coroutineScope.launch {
                                savedListState.animateScrollToItem(0)
                            }
                        }
                    },
                )
            }
        }

        BackHandler(enabled = selectedPage != MainPagerItem.Home) {
            onSelectPage(MainPagerItem.Home)
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
