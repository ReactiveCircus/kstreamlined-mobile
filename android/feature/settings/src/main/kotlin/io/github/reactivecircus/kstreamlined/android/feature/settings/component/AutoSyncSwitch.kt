package io.github.reactivecircus.kstreamlined.android.feature.settings.component

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.feature.settings.R
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Switch
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme

@Composable
internal fun AutoSyncSwitch(
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val bottomCornerRadius by animateDpAsState(if (selected) 0.dp else 16.dp)
    Surface(
        onClick = { onSelectedChange(!selected) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = bottomCornerRadius,
            bottomEnd = bottomCornerRadius,
        ),
        color = KSTheme.colorScheme.container,
        contentColor = KSTheme.colorScheme.onBackgroundVariant,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
                    .padding(vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = stringResource(R.string.setting_auto_sync_title),
                    style = KSTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                    ),
                )

                Text(
                    text = stringResource(R.string.setting_auto_sync_description),
                    style = KSTheme.typography.labelMedium,
                )
            }

            Switch(
                selected = selected,
                onSelectedChange = onSelectedChange,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewAutoSyncSwitch_selected() {
    KSTheme {
        Surface {
            AutoSyncSwitch(
                selected = true,
                onSelectedChange = {},
                modifier = Modifier.padding(24.dp),
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewAutoSyncSwitch_unselected() {
    KSTheme {
        Surface {
            AutoSyncSwitch(
                selected = false,
                onSelectedChange = {},
                modifier = Modifier.padding(24.dp),
            )
        }
    }
}
