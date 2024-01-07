package io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import androidx.compose.material3.HorizontalDivider as MaterialHorizontalDivider
import androidx.compose.material3.VerticalDivider as MaterialVerticalDivider

@Composable
@NonRestartableComposable
public fun HorizontalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = KSTheme.colorScheme.outline,
) {
    MaterialHorizontalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color,
    )
}

@Composable
@NonRestartableComposable
public fun VerticalDivider(
    modifier: Modifier = Modifier,
    thickness: Dp = 1.dp,
    color: Color = KSTheme.colorScheme.outline,
) {
    MaterialVerticalDivider(
        modifier = modifier,
        thickness = thickness,
        color = color,
    )
}

@Composable
@PreviewLightDark
private fun PreviewHorizontalDivider() {
    KSTheme {
        Surface {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(text = "item 1", style = KSTheme.typography.titleMedium)
                HorizontalDivider(
                    modifier = Modifier.width(120.dp),
                )
                Text(text = "item 2", style = KSTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewVerticalDivider() {
    KSTheme {
        Surface {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(text = "item 1", style = KSTheme.typography.titleMedium)
                VerticalDivider(
                    modifier = Modifier.height(120.dp),
                )
                Text(text = "item 2", style = KSTheme.typography.titleMedium)
            }
        }
    }
}
