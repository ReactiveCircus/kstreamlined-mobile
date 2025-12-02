package io.github.reactivecircus.kstreamlined.android.feature.settings.component.sync

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.kmp.presentation.settings.AutoSyncInterval

@Composable
internal fun SyncIntervalPicker(
    selectedSyncInterval: AutoSyncInterval,
    onSelectSyncInterval: (AutoSyncInterval) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        AutoSyncInterval.entries.forEach {
            val isSelected = it == selectedSyncInterval
            Surface(
                onClick = { onSelectSyncInterval(it) },
                color = KSTheme.colorScheme.container,
                contentColor = if (isSelected) {
                    KSTheme.colorScheme.primary
                } else {
                    KSTheme.colorScheme.onBackgroundVariant
                },
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .defaultMinSize(minHeight = 48.dp)
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Box(modifier = Modifier.width(24.dp)) {
                        if (isSelected) {
                            Icon(
                                painter = KSIcons.Check,
                                contentDescription = null,
                            )
                        }
                    }
                    Text(
                        text = syncIntervalOptionLabel(it),
                        style = KSTheme.typography.labelLarge.copy(
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                        ),
                    )
                }
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewSyncIntervalPicker() {
    KSTheme {
        Surface {
            SyncIntervalPicker(
                selectedSyncInterval = AutoSyncInterval.Every6Hours,
                onSelectSyncInterval = {},
            )
        }
    }
}
