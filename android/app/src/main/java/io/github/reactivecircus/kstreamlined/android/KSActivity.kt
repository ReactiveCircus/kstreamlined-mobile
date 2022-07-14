package io.github.reactivecircus.kstreamlined.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import io.github.reactivecircus.kstreamlined.android.theme.KSTheme

@AndroidEntryPoint
class KSActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        // configure edge-to-edge window insets
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            KSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                }
            }
        }
    }
}
