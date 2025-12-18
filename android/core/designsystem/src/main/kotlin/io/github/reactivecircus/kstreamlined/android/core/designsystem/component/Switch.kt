package io.github.reactivecircus.kstreamlined.android.core.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.KSIcons
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import androidx.compose.material3.Switch as MaterialSwitch

@Composable
public fun Switch(
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    MaterialSwitch(
        checked = selected,
        onCheckedChange = onSelectedChange,
        modifier = modifier,
        thumbContent = {
            if (selected) {
                Icon(
                    painter = KSIcons.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = KSTheme.colorScheme.primaryDark,
                )
            }
        },
        colors = SwitchDefaults.colors(
            checkedTrackColor = KSTheme.colorScheme.primary,
            checkedThumbColor = KSTheme.colorScheme.onPrimary,
            uncheckedTrackColor = KSTheme.colorScheme.onTertiaryVariant,
            uncheckedThumbColor = KSTheme.colorScheme.outlineInverse,
            uncheckedBorderColor = KSTheme.colorScheme.outlineVariant,
        ),
    )
}

@Composable
@PreviewLightDark
private fun PreviewSwitch_checked() {
    KSTheme {
        Surface {
            Switch(
                selected = true,
                onSelectedChange = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewSwitch_unchecked() {
    KSTheme {
        Surface {
            Switch(
                selected = false,
                onSelectedChange = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
