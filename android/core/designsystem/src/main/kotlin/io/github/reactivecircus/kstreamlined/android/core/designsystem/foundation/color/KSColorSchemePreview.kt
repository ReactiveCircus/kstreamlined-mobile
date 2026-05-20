package io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.color

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.preview.PreviewKStreamlined

@Composable
@PreviewKStreamlined
private fun PreviewKSColorScheme_accent() {
    Column {
        with(KSTheme.colorScheme) {
            Swatch("accent", accent)
            Swatch("accentStrong", accentStrong)
            Swatch("accentSoft", accentSoft)
            Swatch("onAccent", onAccent)
            Swatch("onAccentMuted", onAccentMuted)
        }
    }
}

@Composable
@PreviewKStreamlined
private fun PreviewKSColorScheme_background() {
    Column {
        with(KSTheme.colorScheme) {
            Swatch("background", background)
            Swatch("onBackground", onBackground)
            Swatch("onBackgroundMuted", onBackgroundMuted)
        }
    }
}

@Composable
@PreviewKStreamlined
private fun PreviewKSColorScheme_surface() {
    Column {
        with(KSTheme.colorScheme) {
            Swatch("surface", surface)
            Swatch("surfaceInverse", surfaceInverse)
            Swatch("surfaceInverseMuted", surfaceInverseMuted)
            Swatch("surfaceInverseRaised", surfaceInverseRaised)
            Swatch("onSurfaceInverse", onSurfaceInverse)
            Swatch("onSurfaceInverseMuted", onSurfaceInverseMuted)
            Swatch("onSurfaceInverseFaint", onSurfaceInverseFaint)
        }
    }
}

@Composable
@PreviewKStreamlined
private fun PreviewKSColorScheme_youtube() {
    Column {
        with(KSTheme.colorScheme) {
            Swatch("surfaceYouTube", surfaceYouTube)
            Swatch("onSurfaceYouTube", onSurfaceYouTube)
            Swatch("onSurfaceYouTubeMuted", onSurfaceYouTubeMuted)
        }
    }
}

@Composable
@PreviewKStreamlined
private fun PreviewKSColorScheme_border() {
    Column {
        with(KSTheme.colorScheme) {
            Swatch("border", border)
            Swatch("borderMuted", borderMuted)
            Swatch("borderInverse", borderInverse)
        }
    }
}

@Composable
@PreviewKStreamlined
private fun PreviewKSColorScheme_brandGradient() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Brush.horizontalGradient(KSTheme.colorScheme.brandGradient))
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart,
    ) {
        Text(
            text = "brandGradient",
            style = KSTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
        )
    }
}

@Composable
private fun Swatch(label: String, color: Color) {
    val onColor = if (color.luminance() < 0.5f) Color.White else Color.Black
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
        color = color,
        contentColor = onColor,
    ) {
        Box(contentAlignment = Alignment.CenterStart) {
            Text(
                text = label,
                style = KSTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(horizontal = 12.dp),
            )
        }
    }
}
