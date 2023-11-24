package io.github.reactivecircus.kstreamlined.android.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.KSIcons

@Composable
internal fun FeedFilterChip(
    selectedFeedCount: Int,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = {},
        modifier = modifier,
        shape = CircleShape,
        color = KSTheme.colorScheme.container,
        contentColor = KSTheme.colorScheme.primaryOnContainer,
    ) {
        Row(
            modifier = Modifier.padding(
                vertical = 8.dp,
                horizontal = 12.dp,
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "$selectedFeedCount Feeds selected".uppercase(), // TODO use string resource
                style = KSTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.sp,
                ),
            )
            Icon(KSIcons.ArrowDown, contentDescription = null)
        }
    }
}
