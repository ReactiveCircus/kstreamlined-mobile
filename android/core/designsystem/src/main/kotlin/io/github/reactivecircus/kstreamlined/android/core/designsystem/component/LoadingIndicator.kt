package io.github.reactivecircus.kstreamlined.android.core.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.preview.PreviewKStreamlined

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
public fun LoadingIndicator(
    modifier: Modifier = Modifier,
    color: Color = KSTheme.colorScheme.accent,
    trackColor: Color = KSTheme.colorScheme.surface,
) {
    ContainedLoadingIndicator(
        modifier = modifier,
        containerColor = trackColor,
        indicatorColor = color,
    )
}

@Composable
@PreviewKStreamlined
private fun PreviewLoadingIndicator() {
    LoadingIndicator(
        modifier = Modifier.padding(8.dp),
    )
}
