package io.github.reactivecircus.kstreamlined.android

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.layout.LazyLayoutCacheWindow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import io.github.reactivecircus.kstreamlined.android.feature.contentviewer.ContentViewerScreen
import io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.KotlinWeeklyIssueScreen
import io.github.reactivecircus.kstreamlined.android.feature.licenses.LicensesScreen
import io.github.reactivecircus.kstreamlined.android.feature.settings.SettingsScreen
import io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.TalkingKotlinEpisodeScreen
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.settings.datasource.SettingsDataSource
import io.github.reactivecircus.kstreamlined.kmp.settings.model.AppSettings
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@OptIn(ExperimentalSharedTransitionApi::class)
@AndroidEntryPoint
class KSActivity : ComponentActivity() {
    @Inject
    lateinit var settingsDataSource: SettingsDataSource

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            var theme by rememberSaveable { mutableStateOf(AppSettings.Theme.System) }
            LaunchedEffect(Unit) {
                settingsDataSource.appSettings.collect { theme = it.theme }
            }

            KSTheme(
                darkTheme = theme.isDarkEffectively,
            ) {
                NavigationBarStyleEffect(theme)

                var navDestination: NavDestination by rememberSaveable {
                    mutableStateOf(NavDestination.Main)
                }

                var selectedNavItem by rememberSaveable { mutableStateOf(NavItemKey.Home) }

                val dpCacheWindow = LazyLayoutCacheWindow(ahead = 300.dp, behind = 300.dp)
                val homeListState = rememberLazyListState(cacheWindow = dpCacheWindow)
                val savedListState = rememberLazyListState(cacheWindow = dpCacheWindow)

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
                                    animatedVisibilityScope = this@AnimatedContent,
                                    selectedNavItem = selectedNavItem,
                                    onSelectedNavItemChanged = { item -> selectedNavItem = item },
                                    homeListState = homeListState,
                                    savedListState = savedListState,
                                    onViewItem = { item, origin ->
                                        navDestination = when (item) {
                                            is FeedItem.KotlinWeekly -> {
                                                NavDestination.KotlinWeeklyIssue(
                                                    boundsKey = "Bounds/$origin/${item.id}",
                                                    topBarBoundsKey = "Bounds/$origin/TopBar",
                                                    titleElementKey = "Element/$origin/TopBar/Title",
                                                    id = item.id,
                                                    issueNumber = item.issueNumber,
                                                )
                                            }

                                            is FeedItem.TalkingKotlin -> {
                                                NavDestination.TalkingKotlinEpisode(
                                                    boundsKey = "Bounds/$origin/${item.id}",
                                                    topBarBoundsKey = "Bounds/$origin/TopBar",
                                                    playerElementKey = "Element/$origin/${item.id}/player",
                                                    id = item.id,
                                                )
                                            }

                                            else -> {
                                                NavDestination.ContentViewer(
                                                    boundsKey = "Bounds/$origin/${item.id}",
                                                    topBarBoundsKey = "Bounds/$origin/TopBar",
                                                    saveButtonElementKey = "Element/$origin/${item.id}/saveButton",
                                                    id = item.id,
                                                )
                                            }
                                        }
                                    },
                                    onOpenSettings = { origin ->
                                        navDestination = NavDestination.Settings(
                                            topBarBoundsKey = "Bounds/$origin/TopBar",
                                            titleElementKey = "Element/$origin/TopBar/Title",
                                        )
                                    },
                                )
                            }

                            is NavDestination.ContentViewer -> {
                                ContentViewerScreen(
                                    animatedVisibilityScope = this@AnimatedContent,
                                    boundsKey = it.boundsKey,
                                    topBarBoundsKey = it.topBarBoundsKey,
                                    saveButtonElementKey = it.saveButtonElementKey,
                                    id = it.id,
                                    onNavigateUp = {
                                        navDestination = NavDestination.Main
                                    },
                                )
                            }

                            is NavDestination.KotlinWeeklyIssue -> {
                                KotlinWeeklyIssueScreen(
                                    animatedVisibilityScope = this@AnimatedContent,
                                    boundsKey = it.boundsKey,
                                    topBarBoundsKey = it.topBarBoundsKey,
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
                                    animatedVisibilityScope = this@AnimatedContent,
                                    boundsKey = it.boundsKey,
                                    topBarBoundsKey = it.topBarBoundsKey,
                                    playerElementKey = it.playerElementKey,
                                    id = it.id,
                                    onNavigateUp = {
                                        navDestination = NavDestination.Main
                                    },
                                )
                            }

                            is NavDestination.Settings -> {
                                SettingsScreen(
                                    animatedVisibilityScope = this@AnimatedContent,
                                    topBarBoundsKey = it.topBarBoundsKey,
                                    titleElementKey = it.titleElementKey,
                                    onOpenLicenses = {
                                        // TODO implement basic backstack
                                        navDestination = NavDestination.Licenses
                                    },
                                    onNavigateUp = {
                                        navDestination = NavDestination.Main
                                    },
                                )
                            }

                            is NavDestination.Licenses -> {
                                LicensesScreen(
                                    animatedVisibilityScope = this@AnimatedContent,
                                    topBarBoundsKey = "Bounds/Licenses/TopBar",
                                    titleElementKey = "Element/Licenses/TopBar/Title",
                                    onNavigateUp = {
                                        navDestination = NavDestination.Main
                                    },
                                )
                            }
                        }
                    }
                }

                // TODO remove once implemented proper navigation
                BackHandler(enabled = navDestination != NavDestination.Main) {
                    navDestination = NavDestination.Main
                }
            }
        }
    }
}

private val AppSettings.Theme.isDarkEffectively: Boolean
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        AppSettings.Theme.System -> isSystemInDarkTheme()
        AppSettings.Theme.Light -> false
        AppSettings.Theme.Dark -> true
    }

@Composable
private fun ComponentActivity.NavigationBarStyleEffect(theme: AppSettings.Theme) {
    val navigationBarColor = KSTheme.colorScheme.background.toArgb()
    val isDarkEffectively = theme.isDarkEffectively
    DisposableEffect(theme, isDarkEffectively) {
        if (isDarkEffectively) {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
                navigationBarStyle = SystemBarStyle.dark(navigationBarColor),
            )
        } else {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
                navigationBarStyle = SystemBarStyle.light(navigationBarColor, navigationBarColor),
            )
        }
        onDispose { }
    }
}

private sealed interface NavDestination : Parcelable {
    @Parcelize
    data object Main : NavDestination

    @Parcelize
    data class ContentViewer(
        val boundsKey: String,
        val topBarBoundsKey: String,
        val saveButtonElementKey: String,
        val id: String,
    ) : NavDestination

    @Parcelize
    data class KotlinWeeklyIssue(
        val boundsKey: String,
        val topBarBoundsKey: String,
        val titleElementKey: String,
        val id: String,
        val issueNumber: Int,
    ) : NavDestination

    @Parcelize
    data class TalkingKotlinEpisode(
        val boundsKey: String,
        val topBarBoundsKey: String,
        val playerElementKey: String,
        val id: String,
    ) : NavDestination

    @Parcelize
    data class Settings(
        val topBarBoundsKey: String,
        val titleElementKey: String,
    ) : NavDestination

    @Parcelize
    data object Licenses : NavDestination
}
