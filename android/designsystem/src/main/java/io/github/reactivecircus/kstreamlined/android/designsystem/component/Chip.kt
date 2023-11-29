package io.github.reactivecircus.kstreamlined.android.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.designsystem.ThemePreviews
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.KSIcons

@Composable
public fun Chip(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = KSTheme.colorScheme.container,
    contentColor: Color = KSTheme.colorScheme.onBackground,
    content: @Composable RowScope.() -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = CircleShape,
        color = color,
        contentColor = contentColor,
    ) {
        Row(
            modifier = Modifier.padding(
                vertical = 8.dp,
                horizontal = 12.dp,
            ),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            content = content,
        )
    }
}

@Composable
@ThemePreviews
private fun PreviewChip() {
    KSTheme {
        Surface {
            Chip(
                onClick = {},
                modifier = Modifier.padding(8.dp),
            ) {
                Text(
                    text = "Chip",
                    style = KSTheme.typography.labelLarge,
                )
                Icon(KSIcons.ArrowDown, contentDescription = null)
            }
        }
    }
}
