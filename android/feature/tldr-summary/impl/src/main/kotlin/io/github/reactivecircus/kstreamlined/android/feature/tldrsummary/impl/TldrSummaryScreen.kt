package io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SharedTransitionScope.ResizeMode.Companion.scaleToBounds
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.tracing.trace
import io.github.reactivecircus.kstreamlined.android.core.animation.NoOpSharedElementLayout
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.TopNavBarSharedTransitionKeys
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.core.designsystem.preview.PreviewKStreamlined
import io.github.reactivecircus.kstreamlined.android.core.ui.markdown.Markdown
import io.github.reactivecircus.kstreamlined.android.core.ui.pattern.ErrorUi
import io.github.reactivecircus.kstreamlined.android.feature.contentviewer.api.ContentViewerRoute
import io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.api.TldrSummaryRoute
import io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.api.TldrSummarySharedTransitionKeys
import io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl.component.DisclosureFooter
import io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl.component.GenerationMetadata
import io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl.component.LoadingSkeletonUi
import io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl.component.TldrBadge
import io.github.reactivecircus.kstreamlined.kmp.capsule.inject.assistedRetainPresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.tldrsummary.TldrSummary
import io.github.reactivecircus.kstreamlined.kmp.presentation.tldrsummary.TldrSummaryPresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.tldrsummary.TldrSummaryUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.tldrsummary.TldrSummaryUiState
import io.github.reactivecircus.routebinding.runtime.RouteBinding

@RouteBinding(TldrSummaryRoute::class)
@Composable
context(sharedTransitionScope: SharedTransitionScope)
internal fun TldrSummaryScreen(
    backStack: NavBackStack<NavKey>,
    route: TldrSummaryRoute,
) = trace("Screen:TldrSummaryScreen") {
    val presenter = assistedRetainPresenter<TldrSummaryPresenter, TldrSummaryPresenter.Factory> {
        create(route.id)
    }
    val uiState by presenter.states.collectAsState()
    val eventSink = presenter.eventSink

    val animatedVisibilityScope = LocalNavAnimatedContentScope.current

    with(sharedTransitionScope) {
        TldrSummaryScreen(
            animatedVisibilityScope = animatedVisibilityScope,
            topBarBoundsKey = TopNavBarSharedTransitionKeys.bounds(route.origin),
            tldrButtonBoundsKey = TldrSummarySharedTransitionKeys.tldrButtonBounds(
                origin = route.origin,
                id = route.id,
            ),
            titleBoundsKey = TldrSummarySharedTransitionKeys.titleBounds(
                origin = route.origin,
                id = route.id,
            ),
            title = route.title,
            onNavigateUp = backStack::removeLastOrNull,
            onReadOriginal = {
                backStack.replaceSingleTop(
                    current = route,
                    destination = ContentViewerRoute(origin = route.origin, id = route.id),
                )
            },
            uiState = uiState,
            eventSink = eventSink,
            modifier = Modifier.sharedBounds(
                sharedContentState = rememberSharedContentState(
                    key = TldrSummarySharedTransitionKeys.bounds(
                        origin = route.origin,
                        id = route.id,
                    ),
                ),
                animatedVisibilityScope = animatedVisibilityScope,
            ),
        )
    }
}

@Composable
context(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
)
internal fun TldrSummaryScreen(
    topBarBoundsKey: String,
    tldrButtonBoundsKey: String,
    titleBoundsKey: String,
    title: String,
    onNavigateUp: () -> Unit,
    onReadOriginal: () -> Unit,
    uiState: TldrSummaryUiState,
    eventSink: (TldrSummaryUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
) = with(sharedTransitionScope) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(KSTheme.colorScheme.background),
    ) {
        HeaderBackdrop(
            modifier = Modifier
                .fillMaxWidth()
                .height(HeaderBackdropHeight),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal)),
        ) {
            Box(
                modifier = Modifier
                    .sharedBounds(
                        sharedContentState = rememberSharedContentState(key = topBarBoundsKey),
                        animatedVisibilityScope = animatedVisibilityScope,
                        zIndexInOverlay = 1f,
                        resizeMode = scaleToBounds(ContentScale.FillWidth, Alignment.TopCenter),
                    )
                    .fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .padding(16.dp),
            ) {
                LargeIconButton(
                    KSIcons.Close,
                    contentDescription = null,
                    onClick = onNavigateUp,
                    modifier = Modifier.align(Alignment.CenterStart),
                )
                TldrBadge(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .sharedBounds(
                            sharedContentState = rememberSharedContentState(key = tldrButtonBoundsKey),
                            animatedVisibilityScope = animatedVisibilityScope,
                            zIndexInOverlay = 1f,
                        ),
                )
            }
            Column(
                modifier = Modifier
                    .topFadingEdge(scrollState)
                    .then(
                        if (uiState is TldrSummaryUiState.Content) {
                            Modifier.verticalScroll(scrollState)
                        } else {
                            Modifier
                        },
                    )
                    .padding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom).asPaddingValues())
                    .padding(top = 8.dp)
                    .padding(horizontal = 24.dp),
            ) {
                Text(
                    text = title,
                    style = KSTheme.typography.titleLarge,
                    color = KSTheme.colorScheme.onBackground,
                    modifier = Modifier.sharedBounds(
                        sharedContentState = rememberSharedContentState(key = titleBoundsKey),
                        animatedVisibilityScope = animatedVisibilityScope,
                        zIndexInOverlay = 1f,
                        resizeMode = scaleToBounds(ContentScale.FillWidth, Alignment.TopStart),
                    ),
                )
                Spacer(modifier = Modifier.height(12.dp))
                AnimatedContent(
                    targetState = uiState,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    contentKey = { state -> state.contentKey },
                    label = "uiState",
                ) { state ->
                    when (state) {
                        is TldrSummaryUiState.Loading -> {
                            LoadingSkeletonUi()
                        }

                        is TldrSummaryUiState.Error -> {
                            ErrorUi(
                                onRetry = { eventSink(TldrSummaryUiEvent.Retry) },
                                modifier = Modifier.padding(bottom = 72.dp),
                            )
                        }

                        is TldrSummaryUiState.Content -> {
                            ContentUi(
                                summary = state.summary,
                                modifier = Modifier.padding(bottom = 72.dp),
                            )
                        }
                    }
                }
            }
        }
        ReadOriginalButton(
            onClick = onReadOriginal,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom).asPaddingValues())
                .padding(horizontal = 24.dp, vertical = 8.dp),
        )
    }
}

private val TldrSummaryUiState.contentKey: Int
    get() = when (this) {
        is TldrSummaryUiState.Loading -> 0
        is TldrSummaryUiState.Error -> 1
        is TldrSummaryUiState.Content -> 2
    }

@Composable
private fun ContentUi(
    summary: TldrSummary,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        GenerationMetadata(
            displayableGeneratedTime = summary.displayableGeneratedTime,
            model = summary.model,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Markdown(markdown = summary.markdownContent)
        Spacer(modifier = Modifier.height(24.dp))
        DisclosureFooter()
    }
}

@Composable
private fun HeaderBackdrop(modifier: Modifier = Modifier) {
    val glowColor = KSTheme.colorScheme.surfaceAccent
    val accentColor = KSTheme.colorScheme.accent
    val backgroundColor = KSTheme.colorScheme.background
    Box(
        modifier = modifier.drawBehind {
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(glowColor, Color.Transparent),
                    center = Offset.Zero,
                    radius = size.width,
                ),
            )
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(accentColor.copy(alpha = AccentGlowAlpha), Color.Transparent),
                    center = Offset(size.width, size.height * AccentGlowCenterYFraction),
                    radius = size.width * AccentGlowRadiusFraction,
                ),
            )
            drawRect(
                brush = Brush.verticalGradient(
                    BackdropFadeStartFraction to Color.Transparent,
                    1f to backgroundColor,
                ),
            )
        },
    )
}

private val HeaderBackdropHeight = 320.dp
private const val AccentGlowAlpha = 0.3f
private const val AccentGlowCenterYFraction = 0.15f
private const val AccentGlowRadiusFraction = 0.8f
private const val BackdropFadeStartFraction = 0.6f

private fun Modifier.topFadingEdge(scrollState: ScrollState): Modifier = this
    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    .drawWithContent {
        drawContent()
        val fadeHeight = minOf(scrollState.value.toFloat(), FadingEdgeHeight.toPx())
        if (fadeHeight > 0f) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    endY = fadeHeight,
                ),
                size = Size(size.width, fadeHeight),
                blendMode = BlendMode.DstIn,
            )
        }
    }

private val FadingEdgeHeight = 24.dp

@Composable
private fun ReadOriginalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        color = KSTheme.colorScheme.surfaceInverse,
        contentColor = KSTheme.colorScheme.onSurfaceInverse,
        elevation = 4.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                KSIcons.ReadMore,
                contentDescription = null,
            )
            Text(
                text = stringResource(R.string.read_original).uppercase(),
                style = KSTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
            )
        }
    }
}

@Composable
@PreviewKStreamlined
private fun PreviewTldrSummaryScreen_Loading() {
    NoOpSharedElementLayout {
        TldrSummaryScreen(
            topBarBoundsKey = "",
            tldrButtonBoundsKey = "",
            titleBoundsKey = "",
            title = SampleTitle,
            onNavigateUp = {},
            onReadOriginal = {},
            uiState = TldrSummaryUiState.Loading,
            eventSink = {},
        )
    }
}

@Composable
@PreviewKStreamlined
private fun PreviewTldrSummaryScreen_Error() {
    NoOpSharedElementLayout {
        TldrSummaryScreen(
            topBarBoundsKey = "",
            tldrButtonBoundsKey = "",
            titleBoundsKey = "",
            title = SampleTitle,
            onNavigateUp = {},
            onReadOriginal = {},
            uiState = TldrSummaryUiState.Error,
            eventSink = {},
        )
    }
}

@Composable
@PreviewKStreamlined
private fun PreviewTldrSummaryScreen_Content() {
    NoOpSharedElementLayout {
        TldrSummaryScreen(
            topBarBoundsKey = "",
            tldrButtonBoundsKey = "",
            titleBoundsKey = "",
            title = SampleTitle,
            onNavigateUp = {},
            onReadOriginal = {},
            uiState = TldrSummaryUiState.Content(SampleTldrSummary),
            eventSink = {},
        )
    }
}

@Composable
@PreviewKStreamlined
private fun PreviewTldrSummaryScreen_Content_ScrolledToBottom() {
    NoOpSharedElementLayout {
        TldrSummaryScreen(
            topBarBoundsKey = "",
            tldrButtonBoundsKey = "",
            titleBoundsKey = "",
            title = SampleTitle,
            onNavigateUp = {},
            onReadOriginal = {},
            uiState = TldrSummaryUiState.Content(SampleTldrSummary),
            eventSink = {},
            scrollState = ScrollState(Int.MAX_VALUE),
        )
    }
}
