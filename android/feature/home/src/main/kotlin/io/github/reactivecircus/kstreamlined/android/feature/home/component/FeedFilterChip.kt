package io.github.reactivecircus.kstreamlined.android.feature.home.component

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Chip
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.feature.home.R

@Composable
internal fun FeedFilterChip(
    showSkeleton: Boolean,
    selectedFeedCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Chip(
        onClick = onClick,
        modifier = modifier,
        enabled = !showSkeleton,
        contentColor = if (showSkeleton) {
            KSTheme.colorScheme.container
        } else {
            KSTheme.colorScheme.primary
        },
    ) {
        Text(
            text = stringResource(id = R.string.feeds_selected, selectedFeedCount).uppercase(),
            style = KSTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.1.sp,
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
                showSkeleton = false,
                selectedFeedCount = 4,
                onClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewFeedFilterChip_skeleton() {
    KSTheme {
        Surface {
            FeedFilterChip(
                showSkeleton = true,
                selectedFeedCount = 0,
                onClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
