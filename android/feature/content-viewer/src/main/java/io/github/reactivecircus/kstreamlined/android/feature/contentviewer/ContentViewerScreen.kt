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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.designsystem.component.FilledIconButton
import io.github.reactivecircus.kstreamlined.android.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.designsystem.component.LinearProgressIndicator
import io.github.reactivecircus.kstreamlined.android.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.BookmarkAdd
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.BookmarkFill
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.KSIcons

@Composable
public fun ContentViewerScreen(
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO load FeedItem by id
    val title = "Kotlin Multiplatform Development Roadmap for 2024"
    val url = "https://blog.jetbrains.com/kotlin/2023/11/kotlin-multiplatform-development-roadmap-for-2024/"

    var saved by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KSTheme.colorScheme.background),
    ) {
        val context = LocalContext.current
        TopNavBar(
            title = "",
            navigationIcon = {
                LargeIconButton(
                    KSIcons.Close,
                    contentDescription = null,
                    onClick = onNavigateUp,
                )
            },
            contentPadding = WindowInsets.statusBars.asPaddingValues(),
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
            modifier = Modifier
                .alpha(animatedAlpha)
                .verticalScroll(rememberScrollState()),
            onCreated = {
                @SuppressLint("SetJavaScriptEnabled")
                it.settings.javaScriptEnabled = true
                it.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            },
        )

        var showProgressIndicator by remember { mutableStateOf(false) }
        LaunchedEffect(state.loadingState) {
            if (state.loadingState != LoadingState.Finished) {
                showProgressIndicator = state.isLoading
            }
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
