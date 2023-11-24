package io.github.reactivecircus.kstreamlined.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.feature.home.HomeScreen

@AndroidEntryPoint
class KSActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            KSTheme {
                val darkTheme = isSystemInDarkTheme()
                val navigationBarColor = KSTheme.colorScheme.background.toArgb()
                LaunchedEffect(darkTheme) {
                    window.navigationBarColor = navigationBarColor
                }

                HomeScreen(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}
