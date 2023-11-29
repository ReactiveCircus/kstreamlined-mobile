package io.github.reactivecircus.kstreamlined.android.designsystem.foundation.color

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
public class KSColorScheme internal constructor(
    public val primary: Color,
    public val primaryDark: Color,
    public val primaryLight: Color,
    public val onPrimary: Color,
    public val onPrimaryVariant: Color,
    public val secondary: Color,
    public val onSecondary: Color,
    public val onSecondaryVariant: Color,
    public val tertiary: Color,
    public val onTertiary: Color,
    public val onTertiaryVariant: Color,
    public val background: Color,
    public val onBackground: Color,
    public val onBackgroundVariant: Color,
    public val container: Color,
    public val outline: Color,
    public val gradient: List<Color>,
    public val isDark: Boolean,
)

internal val LightColorScheme = KSColorScheme(
    primary = Palette.CandyGrapeFizz50,
    primaryDark = Palette.CandyGrapeFizz40,
    primaryLight = Palette.CandyGrapeFizz70,
    onPrimary = Palette.Lavender50,
    onPrimaryVariant = Palette.Lavender40,
    secondary = Palette.Watermelonade40,
    onSecondary = Palette.Watermelonade70,
    onSecondaryVariant = Palette.Watermelonade60,
    tertiary = Color.White,
    onTertiary = Palette.MysteriousDepths10,
    onTertiaryVariant = Palette.MysteriousDepths30,
    background = Palette.Lavender50,
    onBackground = Palette.MysteriousDepths10,
    onBackgroundVariant = Palette.MysteriousDepths30,
    container = Palette.CandyDreams20,
    outline = Palette.CandyDreams10,
    gradient = listOf(
        Palette.CandyGrapeFizz50,
        Palette.PromiscuousPink50,
        Palette.Watermelonade50,
    ),
    isDark = false,
)

internal val DarkColorScheme = KSColorScheme(
    primary = Palette.CandyGrapeFizz60,
    primaryDark = Palette.CandyGrapeFizz40,
    primaryLight = Palette.CandyGrapeFizz70,
    onPrimary = Palette.Lavender40,
    onPrimaryVariant = Palette.Lavender30,
    background = Palette.MysteriousDepths10,
    onBackground = Palette.Lavender50,
    onBackgroundVariant = Palette.Lavender30,
    secondary = Palette.Watermelonade40,
    onSecondary = Palette.Watermelonade70,
    onSecondaryVariant = Palette.Watermelonade60,
    tertiary = Color.White,
    onTertiary = Palette.MysteriousDepths10,
    onTertiaryVariant = Palette.MysteriousDepths30,
    container = Palette.MysteriousDepths20,
    outline = Palette.MysteriousDepths30,
    gradient = listOf(
        Palette.CandyGrapeFizz50,
        Palette.PromiscuousPink50,
        Palette.Watermelonade50,
    ),
    isDark = true,
)

private object Palette {
    val CandyGrapeFizz70 = Color(0xFF_947BFF)
    val CandyGrapeFizz60 = Color(0xFF_8968FF)
    val CandyGrapeFizz50 = Color(0xFF_7F52FF)
    val CandyGrapeFizz40 = Color(0xFF_6E46DE)

    val PromiscuousPink50 = Color(0xFF_C711E1)

    val Watermelonade70 = Color(0xFF_FFEEED)
    val Watermelonade60 = Color(0xFF_FFDDDC)
    val Watermelonade50 = Color(0xFF_E44855)
    val Watermelonade40 = Color(0xFF_C63D49)

    val Lavender50 = Color(0xFF_E7DBFF)
    val Lavender40 = Color(0xFF_DBD0F2)
    val Lavender30 = Color(0xFF_C4BAD9)

    val CandyDreams20 = Color(0xFF_F1C2F9)
    val CandyDreams10 = Color(0xFF_EDB3F7)

    val MysteriousDepths30 = Color(0xFF_2C2F4E)
    val MysteriousDepths20 = Color(0xFF_18193A)
    val MysteriousDepths10 = Color(0xFF_070426)
}

internal val LocalKSColorScheme = staticCompositionLocalOf<KSColorScheme> {
    error("No KSColorScheme provided")
}

public val LocalContentColor: ProvidableCompositionLocal<Color> =
    compositionLocalOf { error("No ContentColor provided") }
