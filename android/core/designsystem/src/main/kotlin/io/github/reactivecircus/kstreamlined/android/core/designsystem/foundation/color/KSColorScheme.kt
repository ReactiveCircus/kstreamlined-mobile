package io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.color

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

public class KSColorScheme internal constructor(
    public val accent: Color,
    public val accentStrong: Color,
    public val accentSoft: Color,
    public val onAccent: Color,
    public val onAccentMuted: Color,
    public val background: Color,
    public val onBackground: Color,
    public val onBackgroundMuted: Color,
    public val surface: Color,
    public val surfaceInverse: Color,
    public val surfaceInverseMuted: Color,
    public val surfaceInverseRaised: Color,
    public val onSurfaceInverse: Color,
    public val onSurfaceInverseMuted: Color,
    public val onSurfaceInverseFaint: Color,
    public val surfaceYouTube: Color,
    public val onSurfaceYouTube: Color,
    public val onSurfaceYouTubeMuted: Color,
    public val border: Color,
    public val borderMuted: Color,
    public val borderInverse: Color,
    public val brandGradient: List<Color>,
    public val isDark: Boolean,
)

private object Palette {
    val Purple10 = Color(0xFF_1A0E5C)
    val Purple20 = Color(0xFF_2A1A8F)
    val Purple30 = Color(0xFF_4A30C2)
    val Purple40 = Color(0xFF_6E46DE)
    val Purple50 = Color(0xFF_7F52FF)
    val Purple60 = Color(0xFF_8968FF)
    val Purple70 = Color(0xFF_947BFF)
    val Purple80 = Color(0xFF_B5A3FF)
    val Purple90 = Color(0xFF_DAD0FF)
    val Purple95 = Color(0xFF_EFE8FF)

    val Magenta40 = Color(0xFF_9D14B3)
    val Magenta50 = Color(0xFF_C711E1)
    val Magenta60 = Color(0xFF_D549E8)
    val Magenta70 = Color(0xFF_D5A1DE)
    val Magenta80 = Color(0xFF_EDB3F7)
    val Magenta85 = Color(0xFF_F1C2F9)
    val Magenta95 = Color(0xFF_FBE6FF)

    val Red40 = Color(0xFF_C63D49)
    val Red50 = Color(0xFF_E44855)
    val Red90 = Color(0xFF_FFDDDC)
    val Red95 = Color(0xFF_FFEEED)

    val Lavender65 = Color(0xFF_ABA2BE)
    val Lavender75 = Color(0xFF_C4BAD9)
    val Lavender85 = Color(0xFF_DBD0F2)
    val Lavender90 = Color(0xFF_E7DBFF)

    val Neutral0 = Color(0xFF_000000)
    val Neutral5 = Color(0xFF_070426)
    val Neutral10 = Color(0xFF_18193A)
    val Neutral15 = Color(0xFF_2C2F4E)
    val Neutral30 = Color(0xFF_5A5E77)
    val Neutral60 = Color(0xFF_A9ABB9)
}

private val BrandGradient = listOf(
    Palette.Purple50,
    Palette.Magenta50,
    Palette.Red50,
)

internal val LightColorScheme = KSColorScheme(
    accent = Palette.Purple50,
    accentStrong = Palette.Purple40,
    accentSoft = Palette.Purple70,
    onAccent = Palette.Lavender90,
    onAccentMuted = Palette.Lavender85,
    background = Palette.Lavender90,
    onBackground = Palette.Neutral5,
    onBackgroundMuted = Palette.Neutral15,
    surface = Palette.Magenta85,
    surfaceInverse = Palette.Neutral5,
    surfaceInverseMuted = Palette.Neutral15,
    surfaceInverseRaised = Palette.Neutral10,
    onSurfaceInverse = Palette.Lavender90,
    onSurfaceInverseMuted = Palette.Lavender85,
    onSurfaceInverseFaint = Palette.Lavender65,
    surfaceYouTube = Palette.Red40,
    onSurfaceYouTube = Palette.Red95,
    onSurfaceYouTubeMuted = Palette.Red90,
    border = Palette.Magenta80,
    borderMuted = Palette.Magenta70,
    borderInverse = Palette.Neutral60,
    brandGradient = BrandGradient,
    isDark = false,
)

internal val DarkColorScheme = KSColorScheme(
    accent = Palette.Purple70,
    accentStrong = Palette.Purple40,
    accentSoft = Palette.Purple70,
    onAccent = Palette.Lavender85,
    onAccentMuted = Palette.Lavender75,
    background = Palette.Neutral5,
    onBackground = Palette.Lavender90,
    onBackgroundMuted = Palette.Magenta80,
    surface = Palette.Neutral10,
    surfaceInverse = Palette.Lavender90,
    surfaceInverseMuted = Palette.Lavender75,
    surfaceInverseRaised = Palette.Magenta85,
    onSurfaceInverse = Palette.Neutral5,
    onSurfaceInverseMuted = Palette.Neutral15,
    onSurfaceInverseFaint = Palette.Neutral30,
    surfaceYouTube = Palette.Red40,
    onSurfaceYouTube = Palette.Red95,
    onSurfaceYouTubeMuted = Palette.Red90,
    border = Palette.Neutral15,
    borderMuted = Palette.Neutral15,
    borderInverse = Palette.Magenta70,
    brandGradient = BrandGradient,

    isDark = true,
)

internal val LocalKSColorScheme = staticCompositionLocalOf<KSColorScheme> {
    error("No KSColorScheme provided")
}

public val LocalContentColor: ProvidableCompositionLocal<Color> =
    compositionLocalOf { error("No ContentColor provided") }
