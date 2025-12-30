package io.github.reactivecircus.kstreamlined.android.feature.contentviewer.impl

import android.annotation.SuppressLint
import android.webkit.WebSettings
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tracing.trace
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.FilledIconButton
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.LoadingIndicator
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.core.launcher.openShareSheet
import io.github.reactivecircus.kstreamlined.android.core.ui.pattern.ItemNotFoundUi
import io.github.reactivecircus.kstreamlined.kmp.presentation.contentviewer.ContentViewerUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.contentviewer.ContentViewerUiState
import kotlinx.coroutines.delay

@Composable
internal fun SharedTransitionScope.ContentViewerScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    boundsKey: String,
    topBarBoundsKey: String,
    saveButtonElementKey: String,
    id: String,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
): Unit = trace("Screen:ContentViewer") {
    val viewModel = hiltViewModel<ContentViewerViewModel, ContentViewerViewModel.Factory>(
        creationCallback = { it.create(id) },
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val eventSink = viewModel.eventSink

    Column(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal))
            .background(KSTheme.colorScheme.background)
            .sharedBounds(
                sharedContentState = rememberSharedContentState(key = boundsKey),
                animatedVisibilityScope = animatedVisibilityScope,
            ),
    ) {
        val context = LocalContext.current
        TopNavBar(
            animatedVisibilityScope = animatedVisibilityScope,
            boundsKey = topBarBoundsKey,
            title = "",
            contentPadding = WindowInsets.statusBars.asPaddingValues(),
            navigationIcon = {
                LargeIconButton(
                    KSIcons.Close,
                    contentDescription = null,
                    onClick = onNavigateUp,
                )
            },
            actions = {
                if (uiState is ContentViewerUiState.Content) {
                    val item = (uiState as ContentViewerUiState.Content).item
                    FilledIconButton(
                        KSIcons.Share,
                        contentDescription = null,
                        onClick = {
                            context.openShareSheet(item.title, item.contentUrl)
                        },
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FilledIconButton(
                        if (item.savedForLater) {
                            KSIcons.BookmarkFill
                        } else {
                            KSIcons.BookmarkAdd
                        },
                        contentDescription = null,
                        onClick = { eventSink(ContentViewerUiEvent.ToggleSavedForLater) },
                        modifier = Modifier.sharedElement(
                            sharedContentState = rememberSharedContentState(key = saveButtonElementKey),
                            animatedVisibilityScope = animatedVisibilityScope,
                            zIndexInOverlay = 1f,
                        ),
                    )
                }
            },
        )

        AnimatedContent(
            targetState = uiState,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            contentAlignment = Alignment.Center,
            contentKey = { state -> state.contentKey },
            label = "uiState",
        ) { state ->
            when (state) {
                is ContentViewerUiState.Initializing -> {
                    Box(modifier = Modifier.fillMaxSize())
                }

                is ContentViewerUiState.NotFound -> {
                    ItemNotFoundUi(modifier = Modifier.padding(24.dp))
                }

                is ContentViewerUiState.Content -> {
                    ContentUi(url = state.item.contentUrl)
                }
            }
        }
    }
}

@Composable
private fun ContentUi(
    url: String,
    modifier: Modifier = Modifier,
) {
    val state = rememberWebViewState(url)
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        val animatedAlpha by animateFloatAsState(
            targetValue = if (state.isLoading) 0f else 1f,
            label = "Alpha",
        )
        WebView(
            state = state,
            modifier = Modifier
                .fillMaxSize()
                .alpha(animatedAlpha),
            onCreated = {
                @SuppressLint("SetJavaScriptEnabled")
                it.settings.javaScriptEnabled = true
                it.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            },
            client = remember { SchemeAwareWebViewClient() },
        )

        var showLoadingIndicator by remember { mutableStateOf(false) }
        LaunchedEffect(state.loadingState) {
            if (state.isLoading) {
                showLoadingIndicator = true
            } else {
                delay(LoadingIndicatorDismissDelayMillis)
                showLoadingIndicator = false
            }
        }
        AnimatedVisibility(
            visible = showLoadingIndicator,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically(),
            modifier = Modifier.align(Alignment.TopCenter),
        ) {
            LoadingIndicator(
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

private const val LoadingIndicatorDismissDelayMillis = 200L

private val ContentViewerUiState.contentKey: Int
    get() = when (this) {
        is ContentViewerUiState.Initializing -> 0
        is ContentViewerUiState.NotFound -> 1
        is ContentViewerUiState.Content -> 2
    }
