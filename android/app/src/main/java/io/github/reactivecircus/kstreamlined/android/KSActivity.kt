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
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import io.github.reactivecircus.kstreamlined.android.feature.contentviewer.ContentViewerScreen
import io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.KotlinWeeklyIssueScreen
import io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.TalkingKotlinEpisodeScreen
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import kotlinx.parcelize.Parcelize

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

                AnimatedContent(
                    navDestination,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(KSTheme.colorScheme.background),
                    contentAlignment = Alignment.Center,
                    label = "NavTransition",
                ) {
                    when (it) {
                        is NavDestination.Main -> {
                            MainScreen(
                                onViewItem = { feedItem ->
                                    navDestination = when (feedItem) {
                                        is FeedItem.KotlinWeekly -> {
                                            NavDestination.KotlinWeeklyIssue(
                                                id = feedItem.id,
                                                issueNumber = feedItem.issueNumber,
                                            )
                                        }
                                        is FeedItem.TalkingKotlin -> {
                                            NavDestination.TalkingKotlinEpisode(
                                                id = feedItem.id,
                                            )
                                        }
                                        else -> {
                                            NavDestination.ContentViewer(
                                                id = feedItem.id,
                                            )
                                        }
                                    }
                                },
                            )
                        }

                        is NavDestination.ContentViewer -> {
                            ContentViewerScreen(
                                id = it.id,
                                onNavigateUp = {
                                    navDestination = NavDestination.Main
                                },
                            )
                        }

                        is NavDestination.KotlinWeeklyIssue -> {
                            KotlinWeeklyIssueScreen(
                                id = it.id,
                                issueNumber = it.issueNumber,
                                onNavigateUp = {
                                    navDestination = NavDestination.Main
                                },
                            )
                        }

                        is NavDestination.TalkingKotlinEpisode -> {
                            TalkingKotlinEpisodeScreen(
                                id = it.id,
                                onNavigateUp = {
                                    navDestination = NavDestination.Main
                                },
                            )
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
        val id: String
    ) : NavDestination

    @Parcelize
    data class KotlinWeeklyIssue(
        val id: String,
        val issueNumber: Int,
    ) : NavDestination

    @Parcelize
    data class TalkingKotlinEpisode(
        val id: String
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
