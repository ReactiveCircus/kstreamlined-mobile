package io.github.reactivecircus.kstreamlined.android.feature.contentviewer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.webkit.WebSettings
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.FilledIconButton
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.LinearProgressIndicator
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.BookmarkAdd
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.BookmarkFill
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons

@Composable
public fun ContentViewerScreen(
    // TODO replace title and url with id and load FeedItem
    title: String, // TODO load FeedItem by id
    url: String, // TODO load FeedItem by id
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var saved by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KSTheme.colorScheme.background),
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
                FilledIconButton(
                    KSIcons.Share,
                    contentDescription = null,
                    onClick = {
                        context.openShareSheet(title, url)
                    },
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilledIconButton(
                    if (saved) {
                        KSIcons.BookmarkFill
                    } else {
                        KSIcons.BookmarkAdd
                    },
                    contentDescription = null,
                    onClick = { saved = !saved },
                )
            },
        )
        ContentUi(url = url)
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

private fun Context.openShareSheet(title: String, url: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TITLE, title)
        putExtra(Intent.EXTRA_TEXT, url)
    }
    startActivity(Intent.createChooser(intent, null))
}
