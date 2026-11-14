package io.github.reactivecircus.kstreamlined.android.feature.kotlinyoutubevideo.impl

import android.content.pm.ActivityInfo
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterExitState
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsIgnoringVisibility
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsIgnoringVisibility
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.computedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.tracing.trace
import coil3.compose.AsyncImage
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Button
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.FilledIconButton
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.HorizontalDivider
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.LoadingIndicator
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.TopNavBarSharedContentKeys
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.core.display.DeviceOrientation
import io.github.reactivecircus.kstreamlined.android.core.display.DeviceOrientationChangeEffect
import io.github.reactivecircus.kstreamlined.android.core.display.FullScreenEffect
import io.github.reactivecircus.kstreamlined.android.core.display.ScreenOrientationEffect
import io.github.reactivecircus.kstreamlined.android.core.launcher.openShareSheet
import io.github.reactivecircus.kstreamlined.android.core.ui.pattern.ItemNotFoundUi
import io.github.reactivecircus.kstreamlined.android.core.ui.util.linkify
import io.github.reactivecircus.kstreamlined.android.core.web.WebViewFullScreenState
import io.github.reactivecircus.kstreamlined.android.feature.kotlinyoutubevideo.api.KotlinYouTubeVideoRoute
import io.github.reactivecircus.kstreamlined.android.feature.kotlinyoutubevideo.api.KotlinYouTubeVideoSharedContentKeys
import io.github.reactivecircus.kstreamlined.android.feature.kotlinyoutubevideo.impl.component.YouTubePlayer
import io.github.reactivecircus.kstreamlined.android.feature.kotlinyoutubevideo.impl.component.YouTubePlayerError
import io.github.reactivecircus.kstreamlined.kmp.capsule.inject.assistedRetainPresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinyoutubevideo.KotlinYouTubeVideo
import io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinyoutubevideo.KotlinYouTubeVideoPresenter
import io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinyoutubevideo.KotlinYouTubeVideoUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.kotlinyoutubevideo.KotlinYouTubeVideoUiState
import io.github.reactivecircus.routebinding.runtime.RouteBinding
import kotlinx.coroutines.flow.drop

@RouteBinding(KotlinYouTubeVideoRoute::class)
@Composable
context(sharedTransitionScope: SharedTransitionScope)
internal fun KotlinYouTubeVideoScreen(
    backStack: NavBackStack<NavKey>,
    route: KotlinYouTubeVideoRoute,
) = trace("Screen:KotlinYouTubeVideo") {
    val presenter = assistedRetainPresenter<KotlinYouTubeVideoPresenter, KotlinYouTubeVideoPresenter.Factory> {
        create(route.id)
    }
    val uiState by presenter.states.collectAsState()
    val eventSink = presenter.eventSink

    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val animatedVisibilityScope = LocalNavAnimatedContentScope.current

    val isFullScreenLayout = isCompactLandscape() && uiState is KotlinYouTubeVideoUiState.Content
    val playerFullScreenState = remember { WebViewFullScreenState() }
    FullScreenHandler(isFullScreenLayout, playerFullScreenState)

    with(sharedTransitionScope) {
        KotlinYouTubeVideoScreen(
            animatedVisibilityScope = animatedVisibilityScope,
            topBarBoundsKey = TopNavBarSharedContentKeys.bounds(route.origin),
            thumbnailBoundsKey = KotlinYouTubeVideoSharedContentKeys.thumbnailBounds(
                origin = route.origin,
                id = route.id,
            ),
            saveButtonBoundsKey = KotlinYouTubeVideoSharedContentKeys.saveButtonBounds(
                origin = route.origin,
                id = route.id,
            ),
            isFullScreenLayout = isFullScreenLayout,
            playerFullScreenState = playerFullScreenState,
            isPlayerEnabled = !isTransitionActive,
            onNavigateUp = backStack::removeLastOrNull,
            onShareButtonClick = { title, url ->
                context.openShareSheet(title, url)
            },
            onWatchOnYouTube = uriHandler::openUri,
            uiState = uiState,
            eventSink = eventSink,
            modifier = Modifier.sharedBounds(
                sharedContentState = rememberSharedContentState(
                    key = KotlinYouTubeVideoSharedContentKeys.bounds(
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
context(sharedTransitionScope: SharedTransitionScope, animatedVisibilityScope: AnimatedVisibilityScope)
internal fun KotlinYouTubeVideoScreen(
    topBarBoundsKey: String,
    thumbnailBoundsKey: String,
    saveButtonBoundsKey: String,
    isFullScreenLayout: Boolean,
    playerFullScreenState: WebViewFullScreenState,
    isPlayerEnabled: Boolean,
    onNavigateUp: () -> Unit,
    onShareButtonClick: (title: String, url: String) -> Unit,
    onWatchOnYouTube: (url: String) -> Unit,
    uiState: KotlinYouTubeVideoUiState,
    eventSink: (KotlinYouTubeVideoUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(if (isFullScreenLayout) Color.Black else KSTheme.colorScheme.background)
            .then(
                if (isFullScreenLayout) {
                    Modifier
                } else {
                    Modifier.windowInsetsPadding(
                        WindowInsets.navigationBarsIgnoringVisibility.only(WindowInsetsSides.Horizontal),
                    )
                },
            ),
    ) {
        if (!isFullScreenLayout) {
            TopNavBar(
                boundsKey = topBarBoundsKey,
                title = "",
                modifier = Modifier.zIndex(1f),
                contentPadding = WindowInsets.statusBarsIgnoringVisibility.asPaddingValues(),
                navigationIcon = {
                    LargeIconButton(
                        KSIcons.Close,
                        contentDescription = null,
                        onClick = onNavigateUp,
                    )
                },
                actions = {
                    if (uiState is KotlinYouTubeVideoUiState.Content) {
                        val video = uiState.video
                        FilledIconButton(
                            KSIcons.Share,
                            contentDescription = null,
                            onClick = { onShareButtonClick(video.title, video.contentUrl) },
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        FilledIconButton(
                            if (video.savedForLater) {
                                KSIcons.BookmarkFill
                            } else {
                                KSIcons.BookmarkAdd
                            },
                            contentDescription = null,
                            onClick = { eventSink(KotlinYouTubeVideoUiEvent.ToggleSavedForLater) },
                            modifier = with(sharedTransitionScope) {
                                Modifier.sharedBounds(
                                    sharedContentState = rememberSharedContentState(key = saveButtonBoundsKey),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    zIndexInOverlay = 1f,
                                )
                            },
                        )
                    }
                },
            )
        }

        when (uiState) {
            is KotlinYouTubeVideoUiState.Initializing -> {
                Box(modifier = Modifier.fillMaxSize())
            }

            is KotlinYouTubeVideoUiState.NotFound -> {
                ItemNotFoundUi(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(24.dp),
                )
            }

            is KotlinYouTubeVideoUiState.Content -> {
                ContentUi(
                    thumbnailBoundsKey = thumbnailBoundsKey,
                    video = uiState.video,
                    isFullScreenLayout = isFullScreenLayout,
                    playerFullScreenState = playerFullScreenState,
                    isPlayerEnabled = isPlayerEnabled,
                    onWatchOnYouTube = onWatchOnYouTube,
                )
            }
        }
    }
}

@Composable
context(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope
)
private fun ContentUi(
    thumbnailBoundsKey: String,
    video: KotlinYouTubeVideo,
    isFullScreenLayout: Boolean,
    playerFullScreenState: WebViewFullScreenState,
    isPlayerEnabled: Boolean,
    onWatchOnYouTube: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (isPlayerEnabled) {
                    Modifier.testTag("kotlinYouTubeVideo:content")
                } else {
                    Modifier
                },
            ),
    ) {
        val playerModifier = if (isFullScreenLayout) {
            Modifier.fillMaxSize()
        } else {
            Modifier
                .fillMaxWidth()
                .aspectRatio(PlayerAspectRatio)
        }
        var playerError by remember(video.videoId) { mutableStateOf<YouTubePlayerError?>(null) }
        if (playerError == null) {
            Box(
                modifier = playerModifier.background(Color.Black),
                contentAlignment = Alignment.Center,
            ) {
                var isPlayerReady by remember(video.videoId, isPlayerEnabled) { mutableStateOf(false) }
                val thumbnailCornerRadius by animatedVisibilityScope.transition.animateDp(
                    label = "ThumbnailCornerRadius",
                ) {
                    if (it == EnterExitState.Visible) 0.dp else 16.dp
                }
                AsyncImage(
                    model = video.thumbnailUrl,
                    contentDescription = null,
                    modifier = with(sharedTransitionScope) {
                        Modifier.sharedBounds(
                            sharedContentState = rememberSharedContentState(key = thumbnailBoundsKey),
                            animatedVisibilityScope = animatedVisibilityScope,
                            zIndexInOverlay = 1f,
                            clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(thumbnailCornerRadius)),
                        )
                    }.fillMaxSize(),
                    placeholder = ColorPainter(KSTheme.colorScheme.surface),
                    error = ColorPainter(KSTheme.colorScheme.surface),
                    contentScale = ContentScale.Crop,
                )
                if (!isPlayerReady && !LocalInspectionMode.current) {
                    LoadingIndicator()
                }
                if (isPlayerEnabled) {
                    val playerAlpha by animateFloatAsState(
                        targetValue = if (isPlayerReady) 1f else 0f,
                        label = "PlayerAlpha",
                    )
                    YouTubePlayer(
                        videoId = video.videoId,
                        fullScreenState = playerFullScreenState,
                        onReady = { isPlayerReady = true },
                        onError = { playerError = it },
                        onOpenUrl = onWatchOnYouTube,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer { alpha = playerAlpha },
                    )
                }
            }
        } else if (playerError == YouTubePlayerError.LoadFailed) {
            PlayerErrorUi(
                thumbnailUrl = video.thumbnailUrl,
                message = stringResource(id = R.string.video_load_error_message),
                actionLabel = stringResource(id = R.string.retry),
                onAction = { playerError = null },
                modifier = playerModifier,
            )
        } else {
            PlayerErrorUi(
                thumbnailUrl = video.thumbnailUrl,
                message = stringResource(id = R.string.video_unavailable_message),
                actionLabel = stringResource(id = R.string.watch_on_youtube),
                onAction = { onWatchOnYouTube(video.contentUrl) },
                modifier = playerModifier,
            )
        }

        if (!isFullScreenLayout) {
            VideoDetails(
                video = video,
                onWatchOnYouTube = onWatchOnYouTube,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
internal fun PlayerErrorUi(
    thumbnailUrl: String,
    message: String,
    actionLabel: String,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.background(Color.Black),
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            model = thumbnailUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(ErrorThumbnailAlpha),
            placeholder = ColorPainter(KSTheme.colorScheme.surface),
            error = ColorPainter(KSTheme.colorScheme.surface),
            contentScale = ContentScale.Crop,
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = message,
                style = KSTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 24.dp),
                textAlign = TextAlign.Center,
            )
            Button(
                text = actionLabel,
                onClick = onAction,
            )
        }
    }
}

@Composable
private fun VideoDetails(
    video: KotlinYouTubeVideo,
    onWatchOnYouTube: (url: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(top = 24.dp)
            .windowInsetsPadding(WindowInsets.navigationBarsIgnoringVisibility.only(WindowInsetsSides.Bottom)),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = video.displayablePublishTime,
                style = KSTheme.typography.labelMedium,
                color = KSTheme.colorScheme.onBackgroundMuted,
            )
            Text(
                text = video.title,
                style = KSTheme.typography.titleLarge,
            )
        }
        val linkStyle = SpanStyle(
            color = KSTheme.colorScheme.accent,
            fontWeight = FontWeight.Bold,
        )
        val description = remember(video.description, linkStyle) {
            video.description.linkify(linkStyle)
        }
        Text(
            text = description,
            style = KSTheme.typography.bodyMedium.copy(
                color = KSTheme.colorScheme.onBackgroundMuted,
            ),
            modifier = Modifier.padding(horizontal = 24.dp),
        )
        Column {
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 24.dp),
            )
            Surface(
                onClick = { onWatchOnYouTube(video.contentUrl) },
                modifier = Modifier.fillMaxWidth(),
                contentColor = KSTheme.colorScheme.accent,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 24.dp,
                            vertical = 16.dp,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(id = R.string.watch_on_youtube),
                        style = KSTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                        ),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(KSIcons.ArrowRight, contentDescription = null)
                }
            }
        }
    }
}

/**
 * Hides the system bars in the full screen layout or when the player is in full screen mode.
 *
 * Exiting full screen mode always returns to the regular layout, by locking portrait orientation
 * in the full screen layout until the device is rotated from portrait to landscape again.
 */
@Composable
private fun FullScreenHandler(
    isFullScreenLayout: Boolean,
    playerFullScreenState: WebViewFullScreenState,
) {
    if (isFullScreenLayout || playerFullScreenState.isFullScreen) {
        FullScreenEffect()
    }

    var isPortraitLocked by remember { mutableStateOf(false) }
    BackHandler(enabled = isFullScreenLayout && !isPortraitLocked && !playerFullScreenState.isFullScreen) {
        isPortraitLocked = true
    }
    val currentIsFullScreenLayout by rememberUpdatedState(isFullScreenLayout)
    LaunchedEffect(playerFullScreenState) {
        snapshotFlow { playerFullScreenState.isFullScreen }
            .drop(1)
            .collect { isFullScreen ->
                if (!isFullScreen && currentIsFullScreenLayout) {
                    isPortraitLocked = true
                }
            }
    }
    if (isPortraitLocked) {
        ScreenOrientationEffect(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        DeviceOrientationChangeEffect { orientation ->
            if (orientation == DeviceOrientation.Landscape) {
                isPortraitLocked = false
            }
        }
    }
}

/**
 * Whether the device is in landscape orientation with a compact height (e.g. a phone).
 */
@Composable
private fun isCompactLandscape(): Boolean {
    val windowInfo = LocalWindowInfo.current
    val isCompactLandscape by remember(windowInfo) {
        computedStateOf {
            val size = windowInfo.containerDpSize
            size.width > size.height && size.height < CompactHeightThreshold
        }
    }
    return isCompactLandscape
}

private const val PlayerAspectRatio = 16f / 9f
private const val ErrorThumbnailAlpha = 0.4f
private val CompactHeightThreshold = 480.dp
