package io.github.reactivecircus.kstreamlined.android

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.feature.contentviewer.impl.contentViewerEntry
import io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.impl.kotlinWeeklyIssueEntry
import io.github.reactivecircus.kstreamlined.android.feature.licenses.impl.licensesEntry
import io.github.reactivecircus.kstreamlined.android.feature.settings.impl.settingsEntry
import io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.impl.talkingKotlinEpisodeEntry
import io.github.reactivecircus.kstreamlined.kmp.settings.model.AppSettings

class KSActivity : ComponentActivity() {
    private val appGraph by lazy { (application as KSApp).appGraph }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        setContent {
            var theme by rememberSaveable { mutableStateOf(AppSettings.Theme.System) }
            LaunchedEffect(Unit) {
                appGraph.settingsDataSource.appSettings.collect { theme = it.theme }
            }

            KSTheme(
                darkTheme = theme.isDarkEffectively,
            ) {
                NavigationBarStyleEffect(theme)

                CompositionLocalProvider(
                    LocalMetroViewModelFactory provides appGraph.metroViewModelFactory,
                ) {
                    val backStack = rememberNavBackStack(MainRoute)

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
                                mainEntry(backStack)
                                contentViewerEntry(backStack)
                                kotlinWeeklyIssueEntry(backStack)
                                talkingKotlinEpisodeEntry(backStack)
                                settingsEntry(backStack)
                                licensesEntry(backStack)
                            },
                        )
                    }
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
