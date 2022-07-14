package io.github.reactivecircus.kstreamlined.android.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import com.google.accompanist.systemuicontroller.rememberSystemUiController

// TODO define color scheme

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF7F52FF),
    background = Color(0xFF070426),
    surface = Color(0xFF070426),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF7F52FF),
    background = Color(0xFFE7DBFF),
    surface = Color(0xFFE7DBFF),
)

@Composable
fun KSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    // TODO customize shapes and typography
    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )

    if (!LocalInspectionMode.current) {
        val useDarkIcons = !darkTheme
        val systemUiController = rememberSystemUiController()
        SideEffect {
            systemUiController.apply {
                setStatusBarColor(Color.Transparent, darkIcons = useDarkIcons)
                setNavigationBarColor(colorScheme.background, darkIcons = useDarkIcons)
            }
        }
    }
}
