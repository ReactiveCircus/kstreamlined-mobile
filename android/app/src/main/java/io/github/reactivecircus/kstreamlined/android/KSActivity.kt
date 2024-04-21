package io.github.reactivecircus.kstreamlined.android

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import io.github.reactivecircus.kstreamlined.android.feature.contentviewer.ContentViewerScreen
import io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.KotlinWeeklyIssueScreen
import io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.TalkingKotlinEpisodeScreen
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import kotlinx.parcelize.Parcelize

@OptIn(ExperimentalComposeUiApi::class, ExperimentalSharedTransitionApi::class)
@AndroidEntryPoint
class KSActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            KSTheme {
                val darkTheme = isSystemInDarkTheme()
                val context = LocalContext.current
                val backgroundColor = KSTheme.colorScheme.background
                DisposableEffect(darkTheme, context) {
                    val navigationBarColor = when (SystemNavigationMode.of(context)) {
                        SystemNavigationMode.Gesture -> Color.Transparent
                        else -> backgroundColor
                    }
                    window.navigationBarColor = navigationBarColor.toArgb()
                    onDispose { }
                }

                var navDestination: NavDestination by rememberSaveable {
                    mutableStateOf(
                        NavDestination.Main
                    )
                }

                var selectedNavItem by rememberSaveable { mutableStateOf(NavItemKey.Home) }
                val homeListState = rememberLazyListState()
                val savedListState = rememberLazyListState()

                SharedTransitionLayout {
                    AnimatedContent(
                        navDestination,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(KSTheme.colorScheme.background)
                            .semantics {
                                testTagsAsResourceId = true
                            },
                        contentAlignment = Alignment.Center,
                        label = "NavTransition",
                    ) {
                        when (it) {
                            is NavDestination.Main -> {
                                MainScreen(
                                    animatedVisibilityScope = this,
                                    selectedNavItem = selectedNavItem,
                                    onSelectedNavItemChanged = { item -> selectedNavItem = item },
                                    homeListState = homeListState,
                                    savedListState = savedListState,
                                    onViewItem = { item, source ->
                                        navDestination = when (item) {
                                            is FeedItem.KotlinWeekly -> {
                                                NavDestination.KotlinWeeklyIssue(
                                                    boundKey = "Bounds/$source/${item.id}",
                                                    titleElementKey = "Element/$source/${item.id}/title",
                                                    id = item.id,
                                                    issueNumber = item.issueNumber,
                                                )
                                            }
                                            is FeedItem.TalkingKotlin -> {
                                                NavDestination.TalkingKotlinEpisode(
                                                    boundsKey = "Bounds/$source/${item.id}",
                                                    playerElementKey = "Element/$source/${item.id}/player",
                                                    id = item.id,
                                                )
                                            }
                                            else -> {
                                                NavDestination.ContentViewer(
                                                    boundsKey = "Bounds/$source/${item.id}",
                                                    saveButtonElementKey = "Element/$source/${item.id}/saveButton",
                                                    id = item.id,
                                                )
                                            }
                                        }
                                    },
                                )
                            }

                            is NavDestination.ContentViewer -> {
                                ContentViewerScreen(
                                    animatedVisibilityScope = this,
                                    boundsKey = it.boundsKey,
                                    saveButtonElementKey = it.saveButtonElementKey,
                                    id = it.id,
                                    onNavigateUp = {
                                        navDestination = NavDestination.Main
                                    },
                                )
                            }

                            is NavDestination.KotlinWeeklyIssue -> {
                                KotlinWeeklyIssueScreen(
                                    animatedVisibilityScope = this,
                                    boundsKey = it.boundKey,
                                    titleElementKey = it.titleElementKey,
                                    id = it.id,
                                    issueNumber = it.issueNumber,
                                    onNavigateUp = {
                                        navDestination = NavDestination.Main
                                    },
                                )
                            }

                            is NavDestination.TalkingKotlinEpisode -> {
                                TalkingKotlinEpisodeScreen(
                                    animatedVisibilityScope = this,
                                    boundsKey = it.boundsKey,
                                    playerElementKey = it.playerElementKey,
                                    id = it.id,
                                    onNavigateUp = {
                                        navDestination = NavDestination.Main
                                    },
                                )
                            }
                        }
                    }
                }

                BackHandler(enabled = navDestination != NavDestination.Main) {
                    if (navDestination != NavDestination.Main) {
                        navDestination = NavDestination.Main
                    }
                }
            }
        }
    }
}

private sealed interface NavDestination : Parcelable {

    @Parcelize
    data object Main : NavDestination

    @Parcelize
    data class ContentViewer(
        val boundsKey: String,
        val saveButtonElementKey: String,
        val id: String,
    ) : NavDestination

    @Parcelize
    data class KotlinWeeklyIssue(
        val boundKey: String,
        val titleElementKey: String,
        val id: String,
        val issueNumber: Int,
    ) : NavDestination

    @Parcelize
    data class TalkingKotlinEpisode(
        val boundsKey: String,
        val playerElementKey: String,
        val id: String,
    ) : NavDestination
}

enum class SystemNavigationMode {
    ThreeButton,
    TwoButton,
    Gesture;

    companion object {
        fun of(context: Context) = entries.getOrNull(
            Settings.Secure.getInt(context.contentResolver, "navigation_mode", -1)
        )
    }
}
