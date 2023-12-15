package io.github.reactivecircus.kstreamlined.android

import android.content.Context
import android.os.Bundle
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

                var navDestination by rememberSaveable { mutableStateOf(NavDestination.Main) }

                AnimatedContent(
                    navDestination,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(KSTheme.colorScheme.background),
                    contentAlignment = Alignment.Center,
                    label = "NavTransition",
                ) {
                    when (it) {
                        NavDestination.Main -> {
                            MainScreen(
                                onViewContent = {
                                    navDestination = NavDestination.ContentViewer
                                },
                            )
                        }

                        NavDestination.ContentViewer -> {
                            ContentViewerScreen(
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

enum class NavDestination {
    Main,
    ContentViewer,
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
