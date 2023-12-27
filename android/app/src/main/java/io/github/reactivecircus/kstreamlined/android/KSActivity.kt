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
import androidx.compose.runtime.LaunchedEffect
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
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.feature.contentviewer.ContentViewerScreen
import io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.KotlinWeeklyIssueScreen
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
                LaunchedEffect(darkTheme, context) {
                    val navigationBarColor = when (SystemNavigationMode.of(context)) {
                        SystemNavigationMode.Gesture -> Color.Transparent
                        else -> backgroundColor
                    }
                    window.navigationBarColor = navigationBarColor.toArgb()
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
                                    navDestination = if (feedItem is FeedItem.KotlinWeekly) {
                                        NavDestination.KotlinWeeklyIssue(
                                            id = feedItem.id,
                                            title = feedItem.title,
                                        )
                                    } else {
                                        NavDestination.ContentViewer(
                                            title = feedItem.title,
                                            url = feedItem.contentUrl,
                                        )
                                    }
                                },
                            )
                        }

                        is NavDestination.ContentViewer -> {
                            ContentViewerScreen(
                                title = it.title,
                                url = it.url,
                                onNavigateUp = {
                                    navDestination = NavDestination.Main
                                },
                            )
                        }

                        is NavDestination.KotlinWeeklyIssue -> {
                            KotlinWeeklyIssueScreen(
                                title = it.title,
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
        val title: String,
        val url: String,
    ) : NavDestination

    @Parcelize
    data class KotlinWeeklyIssue(
        val id: String,
        val title: String,
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
