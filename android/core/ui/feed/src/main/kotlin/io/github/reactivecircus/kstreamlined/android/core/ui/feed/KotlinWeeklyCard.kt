package io.github.reactivecircus.kstreamlined.android.core.ui.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.tracing.trace
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.IconButton
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.kmp.feed.model.DisplayableFeedItem
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.feed.model.toDisplayable
import kotlin.time.Instant

@Composable
public fun KotlinWeeklyCard(
    item: DisplayableFeedItem<FeedItem.KotlinWeekly>,
    onItemClick: (FeedItem.KotlinWeekly) -> Unit,
    onSaveButtonClick: (FeedItem.KotlinWeekly) -> Unit,
    modifier: Modifier = Modifier,
): Unit = trace("FeedItem:KotlinWeeklyCard") {
    val brush = Brush.horizontalGradient(
        colors = listOf(
            KSTheme.colorScheme.primaryDark,
            KSTheme.colorScheme.primaryLight,
        ),
    )
    Surface(
        onClick = { onItemClick(item.value) },
        modifier = modifier
            .drawBehind {
                drawRoundRect(
                    brush = brush,
                    cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx()),
                )
            }
            .fillMaxWidth()
            .testTag("kotlinWeeklyCard"),
        color = Color.Transparent,
        contentColor = KSTheme.colorScheme.onPrimary,
    ) {
        Row(
            modifier = Modifier.padding(vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = item.value.title,
                    style = KSTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = item.displayablePublishTime,
                    style = KSTheme.typography.bodyMedium,
                    color = KSTheme.colorScheme.onPrimaryVariant,
                )
            }
            IconButton(
                if (item.value.savedForLater) {
                    KSIcons.BookmarkFill
                } else {
                    KSIcons.BookmarkAdd
                },
                contentDescription = null,
                onClick = { onSaveButtonClick(item.value) },
                modifier = Modifier
                    .padding(end = 8.dp)
                    .testTag("saveButton"),
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewKotlinWeeklyCard_unsaved() {
    KSTheme {
        Surface {
            KotlinWeeklyCard(
                item = FeedItem.KotlinWeekly(
                    id = "1",
                    title = "Kotlin Weekly #381",
                    publishTime = Instant.parse("2023-11-19T09:13:00Z"),
                    contentUrl = "contentUrl",
                    savedForLater = false,
                    issueNumber = 381,
                ).toDisplayable(displayablePublishTime = "3 hours ago"),
                onItemClick = {},
                onSaveButtonClick = {},
                modifier = Modifier.padding(24.dp),
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewKotlinWeeklyCard_saved() {
    KSTheme {
        Surface {
            KotlinWeeklyCard(
                item = FeedItem.KotlinWeekly(
                    id = "1",
                    title = "Kotlin Weekly #381",
                    publishTime = Instant.parse("2023-11-19T09:13:00Z"),
                    contentUrl = "contentUrl",
                    savedForLater = true,
                    issueNumber = 381,
                ).toDisplayable(displayablePublishTime = "3 hours ago"),
                onItemClick = {},
                onSaveButtonClick = {},
                modifier = Modifier.padding(24.dp),
            )
        }
    }
}
