package io.github.reactivecircus.kstreamlined.android.feature.home.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Chip
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.feature.home.R

@Composable
internal fun FeedFilterChip(
    selectedFeedCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Chip(
        onClick = onClick,
        modifier = modifier,
        contentColor = KSTheme.colorScheme.primary,
    ) {
        Text(
            text = stringResource(id = R.string.feeds_selected, selectedFeedCount).uppercase(),
            style = KSTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.sp,
            ),
        )
        Icon(KSIcons.ArrowDown, contentDescription = null)
    }
}

@Composable
@PreviewLightDark
private fun PreviewFeedFilterChip() {
    KSTheme {
        Surface {
            FeedFilterChip(
                selectedFeedCount = 4,
                onClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
