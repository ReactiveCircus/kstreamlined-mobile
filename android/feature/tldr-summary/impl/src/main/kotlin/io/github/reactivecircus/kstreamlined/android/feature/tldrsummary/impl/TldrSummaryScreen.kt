package io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.runtime.remember
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.tracing.trace
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.core.designsystem.preview.PreviewKStreamlined
import io.github.reactivecircus.kstreamlined.android.core.ui.pattern.ErrorUi
import io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.api.TldrSummaryRoute
import io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl.component.DisclosureFooter
import io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl.component.GenerationMetadata
import io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl.component.LoadingSkeletonUi
import io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl.component.SummaryUi
import io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl.component.TldrBadge
import io.github.reactivecircus.kstreamlined.kmp.capsule.inject.assistedRetainPresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.tldrsummary.TldrSummary
import io.github.reactivecircus.kstreamlined.kmp.presentation.tldrsummary.TldrSummaryPresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.tldrsummary.TldrSummaryUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.tldrsummary.TldrSummaryUiState
import io.github.reactivecircus.routebinding.runtime.RouteBinding

@RouteBinding(TldrSummaryRoute::class)
@Composable
internal fun TldrSummaryScreen(
    backStack: NavBackStack<NavKey>,
    route: TldrSummaryRoute,
) = trace("Screen:TldrSummaryScreen") {
    val presenter = assistedRetainPresenter<TldrSummaryPresenter, TldrSummaryPresenter.Factory> {
        create(route.id)
    }
    val uiState by presenter.states.collectAsState()
    val eventSink = presenter.eventSink

    TldrSummaryScreen(
        tldrSummaryRenderer = TldrSummaryRenderer.PlainText,
        title = route.title,
        uiState = uiState,
        onNavigateUp = backStack::removeLastOrNull,
        onReadOriginal = { /* TODO */ },
        onRetry = { eventSink(TldrSummaryUiEvent.Retry) },
    )
}

// TODO use same Markdown renderer for previews and production
internal sealed interface TldrSummaryRenderer {
    @Composable
    fun rememberTldrSummaryAnnotatedString(markdownContent: String): AnnotatedString

    data object PlainText : TldrSummaryRenderer {
        @Composable
        override fun rememberTldrSummaryAnnotatedString(markdownContent: String): AnnotatedString {
            return remember(markdownContent) { AnnotatedString(markdownContent) }
        }
    }

    data object Sample : TldrSummaryRenderer {
        @Composable
        override fun rememberTldrSummaryAnnotatedString(markdownContent: String): AnnotatedString {
            return rememberSampleTldrSummary()
        }
    }
}

@Composable
context(tldrSummaryRenderer: TldrSummaryRenderer)
internal fun TldrSummaryScreen(
    title: String,
    uiState: TldrSummaryUiState,
    onNavigateUp: () -> Unit,
    onReadOriginal: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
) {
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
                    .fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .padding(8.dp),
            ) {
                LargeIconButton(
                    KSIcons.Close,
                    contentDescription = null,
                    onClick = onNavigateUp,
                    modifier = Modifier.align(Alignment.CenterStart),
                )
                TldrBadge(modifier = Modifier.align(Alignment.Center))
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
                                onRetry = onRetry,
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
context(tldrSummaryRenderer: TldrSummaryRenderer)
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
        SummaryUi(
            summary = with(tldrSummaryRenderer) {
                rememberTldrSummaryAnnotatedString(summary.markdownContent)
            },
        )
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
    TldrSummaryScreen(
        tldrSummaryRenderer = TldrSummaryRenderer.Sample,
        title = SampleTitle,
        uiState = TldrSummaryUiState.Loading,
        onNavigateUp = {},
        onReadOriginal = {},
        onRetry = {},
    )
}

@Composable
@PreviewKStreamlined
private fun PreviewTldrSummaryScreen_Error() {
    TldrSummaryScreen(
        tldrSummaryRenderer = TldrSummaryRenderer.Sample,
        title = SampleTitle,
        uiState = TldrSummaryUiState.Error,
        onNavigateUp = {},
        onReadOriginal = {},
        onRetry = {},
    )
}

@Composable
@PreviewKStreamlined
private fun PreviewTldrSummaryScreen_Content() {
    TldrSummaryScreen(
        tldrSummaryRenderer = TldrSummaryRenderer.Sample,
        title = SampleTitle,
        uiState = TldrSummaryUiState.Content(SampleTldrSummary),
        onNavigateUp = {},
        onReadOriginal = {},
        onRetry = {},
    )
}

@Composable
@PreviewKStreamlined
private fun PreviewTldrSummaryScreen_Content_ScrolledToBottom() {
    TldrSummaryScreen(
        tldrSummaryRenderer = TldrSummaryRenderer.Sample,
        title = SampleTitle,
        uiState = TldrSummaryUiState.Content(SampleTldrSummary),
        onNavigateUp = {},
        onReadOriginal = {},
        onRetry = {},
        scrollState = ScrollState(Int.MAX_VALUE),
    )
}
