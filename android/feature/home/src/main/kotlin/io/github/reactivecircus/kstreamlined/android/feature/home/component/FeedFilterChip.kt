package io.github.reactivecircus.kstreamlined.android.feature.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Chip
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.core.designsystem.preview.PreviewKStreamlined
import io.github.reactivecircus.kstreamlined.android.feature.home.R

@Composable
internal fun FeedFilterChip(
    showSkeleton: Boolean,
    selectedFeedCount: Int,
    totalFeedCount: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val filtered = selectedFeedCount in 1..<totalFeedCount
    Chip(
        onClick = onClick,
        modifier = modifier.testTag("home:feedFilterChip"),
        enabled = !showSkeleton,
        contentColor = if (showSkeleton) {
            KSTheme.colorScheme.surface
        } else {
            KSTheme.colorScheme.accent
        },
        border = if (filtered) {
            BorderStroke(width = 1.dp, color = KSTheme.colorScheme.accent)
        } else {
            null
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
@PreviewKStreamlined
private fun PreviewFeedFilterChip_allSelected() {
    FeedFilterChip(
        showSkeleton = false,
        selectedFeedCount = 4,
        totalFeedCount = 4,
        onClick = {},
        modifier = Modifier.padding(8.dp),
    )
}

@Composable
@PreviewKStreamlined
private fun PreviewFeedFilterChip_someSelected() {
    FeedFilterChip(
        showSkeleton = false,
        selectedFeedCount = 2,
        totalFeedCount = 4,
        onClick = {},
        modifier = Modifier.padding(8.dp),
    )
}

@Composable
@PreviewKStreamlined
private fun PreviewFeedFilterChip_skeleton() {
    FeedFilterChip(
        showSkeleton = true,
        selectedFeedCount = 0,
        totalFeedCount = 4,
        onClick = {},
        modifier = Modifier.padding(8.dp),
    )
}
