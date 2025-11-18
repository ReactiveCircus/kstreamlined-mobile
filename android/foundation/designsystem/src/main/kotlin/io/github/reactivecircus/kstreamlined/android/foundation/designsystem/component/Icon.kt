package io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.color.LocalContentColor
import androidx.compose.material3.Icon as MaterialIcon

@Composable
@NonRestartableComposable
public fun Icon(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    MaterialIcon(
        painter = painter,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint,
    )
}
