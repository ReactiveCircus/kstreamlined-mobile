package io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
public fun LoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = KSTheme.colorScheme.primary,
    trackColor: Color = KSTheme.colorScheme.container,
) {
    ContainedLoadingIndicator(
        modifier = modifier,
        containerColor = trackColor,
        indicatorColor = color,
    )
}

@Composable
@PreviewLightDark
private fun PreviewLoadingIndicator() {
    KSTheme {
        Surface {
            LoadingIndicator(
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
