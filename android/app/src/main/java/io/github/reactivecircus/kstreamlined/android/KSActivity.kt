package io.github.reactivecircus.kstreamlined.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import io.github.reactivecircus.kstreamlined.android.theme.KSTheme

@AndroidEntryPoint
class KSActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            KSTheme {
                val darkTheme = isSystemInDarkTheme()
                val navigationBarColor = MaterialTheme.colorScheme.background.toArgb()
                LaunchedEffect(darkTheme) {
                    window.navigationBarColor = navigationBarColor
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                }
            }
        }
    }
}
