package io.github.reactivecircus.kstreamlined.android

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.saveable.rememberSerializable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.LocalNavAnimatedContentScope
import androidx.navigation3.ui.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.feature.contentviewer.ContentViewerScreen
import io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.KotlinWeeklyIssueScreen
import io.github.reactivecircus.kstreamlined.android.feature.licenses.LicensesScreen
import io.github.reactivecircus.kstreamlined.android.feature.settings.SettingsScreen
import io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.TalkingKotlinEpisodeScreen
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.settings.datasource.SettingsDataSource
import io.github.reactivecircus.kstreamlined.kmp.settings.model.AppSettings
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

                val backStack = rememberNavBackStack(NavRoute.Main)
                var selectedPage by rememberSerializable { mutableStateOf(MainNavRoute.Home) }

                val dpCacheWindow = LazyLayoutCacheWindow(ahead = 300.dp, behind = 300.dp)
                val homeListState = rememberLazyListState(cacheWindow = dpCacheWindow)
                val savedListState = rememberLazyListState(cacheWindow = dpCacheWindow)

                SharedTransitionLayout {
                    NavDisplay(
                        backStack = backStack,
                        modifier = Modifier.fillMaxSize()
                            .background(KSTheme.colorScheme.background)
                            .semantics { testTagsAsResourceId = true },
                        contentAlignment = Alignment.Center,
                        entryDecorators = listOf(
                            rememberSaveableStateHolderNavEntryDecorator(),
                            rememberViewModelStoreNavEntryDecorator(),
                        ),
                        sharedTransitionScope = this,
                        entryProvider = entryProvider {
                            entry<NavRoute.Main> {
                                MainScreen(
                                    animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                                    selectedPage = selectedPage,
                                    onSelectPage = { page -> selectedPage = page },
                                    homeListState = homeListState,
                                    savedListState = savedListState,
                                    onViewItem = { item, origin ->
                                        backStack.add(
                                            when (item) {
                                                is FeedItem.KotlinWeekly -> {
                                                    NavRoute.KotlinWeeklyIssue(
                                                        boundsKey = "Bounds/$origin/${item.id}",
                                                        topBarBoundsKey = "Bounds/$origin/TopBar",
                                                        titleElementKey = "Element/$origin/TopBar/Title",
                                                        id = item.id,
                                                        issueNumber = item.issueNumber,
                                                    )
                                                }

                                                is FeedItem.TalkingKotlin -> {
                                                    NavRoute.TalkingKotlinEpisode(
                                                        boundsKey = "Bounds/$origin/${item.id}",
                                                        topBarBoundsKey = "Bounds/$origin/TopBar",
                                                        playerElementKey = "Element/$origin/${item.id}/player",
                                                        id = item.id,
                                                    )
                                                }

                                                else -> {
                                                    NavRoute.ContentViewer(
                                                        boundsKey = "Bounds/$origin/${item.id}",
                                                        topBarBoundsKey = "Bounds/$origin/TopBar",
                                                        saveButtonElementKey = "Element/$origin/${item.id}/saveButton",
                                                        id = item.id,
                                                    )
                                                }
                                            },
                                        )
                                    },
                                    onOpenSettings = { origin ->
                                        backStack.add(
                                            NavRoute.Settings(
                                                topBarBoundsKey = "Bounds/$origin/TopBar",
                                                titleElementKey = "Element/$origin/TopBar/Title",
                                            ),
                                        )
                                    },
                                )
                            }
                            entry<NavRoute.ContentViewer> {
                                ContentViewerScreen(
                                    animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                                    boundsKey = it.boundsKey,
                                    topBarBoundsKey = it.topBarBoundsKey,
                                    saveButtonElementKey = it.saveButtonElementKey,
                                    id = it.id,
                                    onNavigateUp = {
                                        backStack.removeLastOrNull()
                                    },
                                )
                            }
                            entry<NavRoute.KotlinWeeklyIssue> {
                                KotlinWeeklyIssueScreen(
                                    animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                                    boundsKey = it.boundsKey,
                                    topBarBoundsKey = it.topBarBoundsKey,
                                    titleElementKey = it.titleElementKey,
                                    id = it.id,
                                    issueNumber = it.issueNumber,
                                    onNavigateUp = {
                                        backStack.removeLastOrNull()
                                    },
                                )
                            }
                            entry<NavRoute.TalkingKotlinEpisode> {
                                TalkingKotlinEpisodeScreen(
                                    animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                                    boundsKey = it.boundsKey,
                                    topBarBoundsKey = it.topBarBoundsKey,
                                    playerElementKey = it.playerElementKey,
                                    id = it.id,
                                    onNavigateUp = {
                                        backStack.removeLastOrNull()
                                    },
                                )
                            }
                            entry<NavRoute.Settings> {
                                SettingsScreen(
                                    animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                                    topBarBoundsKey = it.topBarBoundsKey,
                                    titleElementKey = it.titleElementKey,
                                    onOpenLicenses = {
                                        backStack.add(
                                            NavRoute.Licenses(
                                                boundsKey = "Bounds/LicensesTile",
                                            ),
                                        )
                                    },
                                    onNavigateUp = {
                                        backStack.removeLastOrNull()
                                    },
                                )
                            }
                            entry<NavRoute.Licenses> {
                                LicensesScreen(
                                    animatedVisibilityScope = LocalNavAnimatedContentScope.current,
                                    boundsKey = it.boundsKey,
                                    onNavigateUp = {
                                        backStack.removeLastOrNull()
                                    },
                                )
                            }
                        },
                    )
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
