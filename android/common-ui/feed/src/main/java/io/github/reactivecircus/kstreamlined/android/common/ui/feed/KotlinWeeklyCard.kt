package io.github.reactivecircus.kstreamlined.android.common.ui.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.designsystem.ThemePreviews
import io.github.reactivecircus.kstreamlined.android.designsystem.component.IconButton
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.BookmarkAdd
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.BookmarkFill
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem

@Composable
public fun KotlinWeeklyCard(
    item: FeedItem.KotlinWeekly,
    onItemClick: (FeedItem.KotlinWeekly) -> Unit,
    onSaveButtonClick: (FeedItem.KotlinWeekly) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = { onItemClick(item) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = KSTheme.colorScheme.primary,
        contentColor = KSTheme.colorScheme.onPrimary,
        elevation = 4.dp,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = item.title,
                    style = KSTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = item.publishTime,
                    style = KSTheme.typography.bodyMedium,
                )
            }
            IconButton(
                if (item.savedForLater) {
                    KSIcons.BookmarkFill
                } else {
                    KSIcons.BookmarkAdd
                },
                contentDescription = null,
                onClick = { onSaveButtonClick(item) },
                modifier = Modifier.padding(end = 8.dp),
            )
        }
    }
}

@Composable
@ThemePreviews
private fun PreviewKotlinWeeklyCard_unsaved() {
    KSTheme {
        Surface {
            KotlinWeeklyCard(
                item = FeedItem.KotlinWeekly(
                    id = "1",
                    title = "Kotlin Weekly #381",
                    publishTime = "Moments ago",
                    contentUrl = "contentUrl",
                    savedForLater = false,
                ),
                onItemClick = {},
                onSaveButtonClick = {},
                modifier = Modifier.padding(24.dp),
            )
        }
    }
}

@Composable
@ThemePreviews
private fun PreviewKotlinWeeklyCard_saved() {
    KSTheme {
        Surface {
            KotlinWeeklyCard(
                item = FeedItem.KotlinWeekly(
                    id = "1",
                    title = "Kotlin Weekly #381",
                    publishTime = "3 hours ago",
                    contentUrl = "contentUrl",
                    savedForLater = true,
                ),
                onItemClick = {},
                onSaveButtonClick = {},
                modifier = Modifier.padding(24.dp),
            )
        }
    }
}
