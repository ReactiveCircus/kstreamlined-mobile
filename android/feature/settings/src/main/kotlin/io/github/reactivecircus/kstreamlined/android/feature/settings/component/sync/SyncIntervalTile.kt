package io.github.reactivecircus.kstreamlined.android.feature.settings.component.sync

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.feature.settings.R
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.kmp.presentation.settings.AutoSyncInterval

@Composable
internal fun SyncIntervalTile(
    selectedSyncInterval: AutoSyncInterval,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
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
                    text = stringResource(R.string.setting_sync_interval_title),
                    style = KSTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                    ),
                )

                Text(
                    text = syncIntervalOptionLabel(selectedSyncInterval),
                    style = KSTheme.typography.labelMedium,
                )
            }

            Icon(
                painter = KSIcons.ArrowDown,
                contentDescription = null,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Composable
@ReadOnlyComposable
internal fun syncIntervalOptionLabel(autoSyncInterval: AutoSyncInterval): String {
    return when (autoSyncInterval) {
        AutoSyncInterval.Hourly -> stringResource(R.string.sync_interval_option_hourly)
        AutoSyncInterval.Every3Hours -> stringResource(R.string.sync_interval_option_every_3_hours)
        AutoSyncInterval.Every6Hours -> stringResource(R.string.sync_interval_option_every_6_hours)
        AutoSyncInterval.Every12Hours -> stringResource(R.string.sync_interval_option_every_12_hours)
        AutoSyncInterval.Daily -> stringResource(R.string.sync_interval_option_daily)
    }
}

@Composable
@PreviewLightDark
private fun PreviewSyncIntervalTile() {
    KSTheme {
        Surface {
            SyncIntervalTile(
                selectedSyncInterval = AutoSyncInterval.Every6Hours,
                onClick = {},
                modifier = Modifier.padding(24.dp),
            )
        }
    }
}
