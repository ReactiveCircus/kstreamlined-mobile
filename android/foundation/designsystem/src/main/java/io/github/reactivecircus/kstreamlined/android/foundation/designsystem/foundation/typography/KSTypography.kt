package io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.typography

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.R

public class KSTypography internal constructor(
    public val headlineLarge: TextStyle,
    public val headlineMedium: TextStyle,
    public val headlineSmall: TextStyle,
    public val titleLarge: TextStyle,
    public val titleMedium: TextStyle,
    public val titleSmall: TextStyle,
    public val bodyLarge: TextStyle,
    public val bodyMedium: TextStyle,
    public val bodySmall: TextStyle,
    public val labelLarge: TextStyle,
    public val labelMedium: TextStyle,
    public val labelSmall: TextStyle,
) {
    @Suppress("CyclomaticComplexMethod")
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is KSTypography) return false

        if (headlineLarge != other.headlineLarge) return false
        if (headlineMedium != other.headlineMedium) return false
        if (headlineSmall != other.headlineSmall) return false
        if (titleLarge != other.titleLarge) return false
        if (titleMedium != other.titleMedium) return false
        if (titleSmall != other.titleSmall) return false
        if (bodyLarge != other.bodyLarge) return false
        if (bodyMedium != other.bodyMedium) return false
        if (bodySmall != other.bodySmall) return false
        if (labelLarge != other.labelLarge) return false
        if (labelMedium != other.labelMedium) return false
        if (labelSmall != other.labelSmall) return false

        return true
    }

    override fun hashCode(): Int {
        var result = headlineLarge.hashCode()
        result = 31 * result + headlineMedium.hashCode()
        result = 31 * result + headlineSmall.hashCode()
        result = 31 * result + titleLarge.hashCode()
        result = 31 * result + titleMedium.hashCode()
        result = 31 * result + titleSmall.hashCode()
        result = 31 * result + bodyLarge.hashCode()
        result = 31 * result + bodyMedium.hashCode()
        result = 31 * result + bodySmall.hashCode()
        result = 31 * result + labelLarge.hashCode()
        result = 31 * result + labelMedium.hashCode()
        result = 31 * result + labelSmall.hashCode()
        return result
    }

    @Suppress("MaxLineLength")
    override fun toString(): String {
        return "KSTypography(headlineLarge=$headlineLarge, headlineMedium=$headlineMedium, headlineSmall=$headlineSmall, " +
            "titleLarge=$titleLarge, titleMedium=$titleMedium, titleSmall=$titleSmall, " +
            "bodyLarge=$bodyLarge, bodyMedium=$bodyMedium, bodySmall=$bodySmall, " +
            "labelLarge=$labelLarge, labelMedium=$labelMedium, labelSmall=$labelSmall)"
    }

    internal companion object {
        internal val Default: KSTypography = KSTypography(
            headlineLarge = TextStyle(
                fontFamily = JetBrainsMonoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 32.sp,
                lineHeight = 40.sp,
                letterSpacing = 0.sp,
            ),
            headlineMedium = TextStyle(
                fontFamily = JetBrainsMonoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 28.sp,
                lineHeight = 36.sp,
                letterSpacing = 0.sp,
            ),
            headlineSmall = TextStyle(
                fontFamily = JetBrainsMonoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.sp,
            ),
            titleLarge = TextStyle(
                fontFamily = JetBrainsMonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp,
            ),
            titleMedium = TextStyle(
                fontFamily = JetBrainsMonoFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.1.sp,
            ),
            titleSmall = TextStyle(
                fontFamily = JetBrainsMonoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.1.sp,
            ),
            bodyLarge = TextStyle(
                fontFamily = JetBrainsMonoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp,
            ),
            bodyMedium = TextStyle(
                fontFamily = JetBrainsMonoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.25.sp,
            ),
            bodySmall = TextStyle(
                fontFamily = JetBrainsMonoFontFamily,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.4.sp,
            ),
            labelLarge = TextStyle(
                fontFamily = JetBrainsMonoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                letterSpacing = 0.1.sp,
            ),
            labelMedium = TextStyle(
                fontFamily = JetBrainsMonoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp,
            ),
            labelSmall = TextStyle(
                fontFamily = JetBrainsMonoFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.sp,
            ),
        )
    }
}

internal val LocalKSTypography = staticCompositionLocalOf { KSTypography.Default }

private val JetBrainsMonoFontFamily = FontFamily(
    Font(R.font.jetbrains_mono_regular, FontWeight.Normal),
    Font(R.font.jetbrains_mono_medium, FontWeight.Medium),
    Font(R.font.jetbrains_mono_bold, FontWeight.Bold),
    Font(R.font.jetbrains_mono_extra_bold, FontWeight.ExtraBold),
)
