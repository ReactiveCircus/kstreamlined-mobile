package io.github.reactivecircus.kstreamlined.android.feature.feedselection.impl

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.tracing.trace
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Switch
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.TopNavBarSharedTransitionKeys
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.feature.feedselection.api.FeedSelectionRoute
import io.github.reactivecircus.kstreamlined.android.feature.feedselection.impl.component.FeedSourceCard
import io.github.reactivecircus.kstreamlined.kmp.capsule.inject.retainPresenter
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedOrigin
import io.github.reactivecircus.kstreamlined.kmp.presentation.feedselection.FeedSelectionPresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.feedselection.FeedSelectionUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.feedselection.FeedSelectionUiState
import io.github.reactivecircus.routebinding.runtime.RouteBinding

@RouteBinding(FeedSelectionRoute::class)
@Composable
internal fun SharedTransitionScope.FeedSelectionScreen(
    backStack: NavBackStack<NavKey>,
    route: FeedSelectionRoute,
) = trace("Screen:FeedSelection") {
    val presenter = retainPresenter<FeedSelectionPresenter>()
    val uiState by presenter.states.collectAsState()
    val eventSink = presenter.eventSink

    val animatedVisibilityScope = LocalNavAnimatedContentScope.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
            .background(KSTheme.colorScheme.background),
    ) {
        TopNavBar(
            animatedVisibilityScope = animatedVisibilityScope,
            boundsKey = TopNavBarSharedTransitionKeys.bounds(route.origin),
            titleElementKey = TopNavBarSharedTransitionKeys.titleElement(route.origin),
            title = stringResource(id = R.string.title_feed_sources),
            modifier = Modifier.zIndex(1f),
            contentPadding = WindowInsets.statusBars.asPaddingValues(),
            navigationIcon = {
                LargeIconButton(
                    KSIcons.Close,
                    contentDescription = null,
                    onClick = backStack::removeLastOrNull,
                )
            },
        )

        when (val state = uiState) {
            is FeedSelectionUiState.Loading -> {}
            is FeedSelectionUiState.Content -> {
                FeedSourceList(
                    feedOrigins = state.feedOrigins,
                    onToggle = { key ->
                        eventSink(FeedSelectionUiEvent.ToggleFeedOrigin(key))
                    },
                )
            }
        }
    }
}

@Composable
private fun FeedSourceList(
    feedOrigins: List<FeedOrigin>,
    onToggle: (FeedOrigin.Key) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        feedOrigins.forEach { origin ->
            FeedSourceCard(
                origin = origin,
                onToggle = { onToggle(origin.key) },
            )
        }
        Spacer(
            modifier = Modifier.height(
                WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
            ),
        )
    }
}
