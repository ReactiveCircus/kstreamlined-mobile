@file:OptIn(ExperimentalSharedTransitionApi::class)

package io.github.reactivecircus.kstreamlined.android.feature.contentviewer

import android.annotation.SuppressLint
import android.webkit.WebSettings
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tracing.trace
import coil3.compose.AsyncImage
import io.github.reactivecircus.kstreamlined.android.feature.common.openShareSheet
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.FilledIconButton
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.LinearProgressIndicator
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.BookmarkAdd
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.BookmarkFill
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.kmp.presentation.contentviewer.ContentViewerUiEvent
import io.github.reactivecircus.kstreamlined.kmp.presentation.contentviewer.ContentViewerUiState
import io.github.reactivecircus.kstreamlined.android.feature.common.R as commonR

@Composable
public fun SharedTransitionScope.ContentViewerScreen(
    animatedVisibilityScope: AnimatedVisibilityScope,
    boundsKey: String,
    saveButtonElementKey: String,
    id: String,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
): Unit = trace("Screen:ContentViewer") {
    val viewModel = viewModel<ContentViewerViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val eventSink = viewModel.eventSink

    LaunchedEffect(id) {
        eventSink(ContentViewerUiEvent.LoadContent(id))
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KSTheme.colorScheme.background)
            .sharedBounds(
                rememberSharedContentState(key = boundsKey),
                animatedVisibilityScope = animatedVisibilityScope,
            ),
    ) {
        val context = LocalContext.current
        TopNavBar(
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
                            rememberSharedContentState(key = saveButtonElementKey),
                            animatedVisibilityScope = animatedVisibilityScope,
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
                    ItemNotFoundUi()
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
            modifier = Modifier.alpha(animatedAlpha),
            onCreated = {
                @SuppressLint("SetJavaScriptEnabled")
                it.settings.javaScriptEnabled = true
                it.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            },
            client = remember { SchemeAwareWebViewClient() },
        )

        var showProgressIndicator by remember { mutableStateOf(false) }
        DisposableEffect(state.loadingState) {
            if (state.loadingState != LoadingState.Finished) {
                showProgressIndicator = state.isLoading
            }
            onDispose { }
        }
        AnimatedVisibility(
            visible = showProgressIndicator,
            modifier = Modifier.fillMaxWidth(),
        ) {
            LinearProgressIndicator(
                progress = {
                    when (val loadingState = state.loadingState) {
                        is LoadingState.Initializing -> 0f
                        is LoadingState.Loading -> loadingState.progress
                        is LoadingState.Finished -> 1f
                    }
                },
                onProgressAnimationEnd = {
                    showProgressIndicator = false
                },
            )
        }
    }
}

@Composable
private fun ItemNotFoundUi(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AsyncImage(
            commonR.drawable.ic_kodee_broken_hearted,
            contentDescription = null,
            modifier = Modifier.size(160.dp),
        )
        Spacer(modifier = Modifier.height(36.dp))
        Text(
            text = stringResource(id = commonR.string.content_not_found_message),
            style = KSTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = TextAlign.Center,
        )
    }
}

private val ContentViewerUiState.contentKey: Int
    get() = when (this) {
        is ContentViewerUiState.Initializing -> 0
        is ContentViewerUiState.NotFound -> 1
        is ContentViewerUiState.Content -> 2
    }
