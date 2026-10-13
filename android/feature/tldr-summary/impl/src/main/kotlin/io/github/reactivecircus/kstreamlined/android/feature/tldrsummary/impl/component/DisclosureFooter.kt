package io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.HorizontalDashedDivider
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.core.designsystem.preview.PreviewKStreamlined
import io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl.R

@Composable
internal fun DisclosureFooter(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        HorizontalDashedDivider(
            color = KSTheme.colorScheme.borderMuted,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                KSIcons.Info,
                contentDescription = null,
                modifier = Modifier.requiredSize(16.dp),
                tint = KSTheme.colorScheme.onBackgroundMuted,
            )
            Text(
                text = stringResource(R.string.tldr_disclosure),
                style = KSTheme.typography.bodySmall,
                color = KSTheme.colorScheme.onBackgroundMuted,
            )
        }
    }
}

@Composable
@PreviewKStreamlined
private fun PreviewDisclosureFooter() {
    DisclosureFooter(modifier = Modifier.padding(24.dp))
}
