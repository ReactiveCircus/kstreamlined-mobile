package io.github.reactivecircus.kstreamlined.android.designsystem.foundation

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.color.DarkColorScheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.color.KSColorScheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.color.LightColorScheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.color.LocalContentColor
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.color.LocalKSColorScheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.typography.KSTypography
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.typography.LocalKSTypography

@Composable
public fun KSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    CompositionLocalProvider(
        LocalKSColorScheme provides colorScheme,
        LocalContentColor provides colorScheme.onBackground,
        LocalKSTypography provides KSTypography.Default,
    ) {
        MaterialTheme(
            content = content,
        )
    }
}

public object KSTheme {
    public val colorScheme: KSColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalKSColorScheme.current
    public val typography: KSTypography
        @Composable
        @ReadOnlyComposable
        get() = LocalKSTypography.current
}
