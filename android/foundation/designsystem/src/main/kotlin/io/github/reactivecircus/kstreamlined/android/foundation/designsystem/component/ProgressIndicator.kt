package io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import androidx.compose.material3.LinearProgressIndicator as MaterialLinearProgressIndicator

@Composable
public fun LinearProgressIndicator(
    progress: () -> Float,
    modifier: Modifier = Modifier,
    onProgressAnimationEnd: () -> Unit = {},
    color: Color = KSTheme.colorScheme.primary,
    trackColor: Color = KSTheme.colorScheme.container,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress(),
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "ProgressAnimation",
        finishedListener = { onProgressAnimationEnd() },
    )
    MaterialLinearProgressIndicator(
        progress = { animatedProgress },
        modifier = modifier,
        color = color,
        trackColor = trackColor,
    )
}

@Composable
@PreviewLightDark
private fun PreviewLinearProgressIndicator() {
    KSTheme {
        Surface {
            LinearProgressIndicator(
                progress = { 0.5f },
                modifier = Modifier
                    .width(200.dp)
                    .padding(8.dp),
            )
        }
    }
}
