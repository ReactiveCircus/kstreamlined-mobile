package io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component

import android.widget.Switch
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons
import androidx.compose.material3.Switch as MaterialSwitch

@Composable
public fun Switch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = KSTheme.colorScheme.container,
    contentColor: Color = KSTheme.colorScheme.primary,
) {
    MaterialSwitch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        thumbContent = {
//            Icon(KSIcons.Close, contentDescription = null)
        },
        enabled = enabled,
        colors = SwitchDefaults.colors(
            checkedTrackColor = KSTheme.colorScheme.primary,
            checkedThumbColor = KSTheme.colorScheme.onPrimary,
//            checkedBorderColor = KSTheme.colorScheme.primary
        )
    )
//    Surface(
//        onClick = onClick,
//        modifier = modifier.semantics { role = Role.Button },
//        enabled = enabled,
//        shape = CircleShape,
//        color = color,
//        contentColor = contentColor,
//    ) {
//        Row(
//            modifier = Modifier.padding(
//                vertical = 12.dp,
//                horizontal = 16.dp,
//            ),
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Text(
//                text = text.uppercase(),
//                style = KSTheme.typography.labelLarge.copy(
//                    fontWeight = FontWeight.ExtraBold,
//                ),
//            )
//        }
//    }
}

@Composable
@PreviewLightDark
private fun PreviewSwitch_checked() {
    KSTheme {
        Surface {
            Switch(
                checked = true,
                onCheckedChange = {},
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
                checked = false,
                onCheckedChange = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
