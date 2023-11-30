package io.github.reactivecircus.kstreamlined.android.common.ui.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import io.github.reactivecircus.kstreamlined.android.designsystem.ThemePreviews
import io.github.reactivecircus.kstreamlined.android.designsystem.component.IconButton
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.BookmarkAdd
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.BookmarkFill
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import kotlinx.datetime.toInstant

@Composable
public fun TalkingKotlinCard(
    item: DisplayableFeedItem<FeedItem.TalkingKotlin>,
    onItemClick: (FeedItem.TalkingKotlin) -> Unit,
    onSaveButtonClick: (FeedItem.TalkingKotlin) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = { onItemClick(item.value) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = KSTheme.colorScheme.tertiary,
        contentColor = KSTheme.colorScheme.onTertiary,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = item.value.podcastLogoUrl,
                contentDescription = null,
                modifier = Modifier
                    .padding(8.dp)
                    .size(80.dp),
                contentScale = ContentScale.Fit,
            )
            Column(
                modifier = Modifier.padding(
                    end = 8.dp,
                    top = 24.dp,
                    bottom = 8.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = item.value.title,
                    style = KSTheme.typography.titleLarge,
                    modifier = Modifier.padding(end = 8.dp),
                    overflow = TextOverflow.Ellipsis,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.displayablePublishTime,
                        style = KSTheme.typography.bodyMedium,
                        color = KSTheme.colorScheme.onTertiaryVariant,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(
                        if (item.value.savedForLater) {
                            KSIcons.BookmarkFill
                        } else {
                            KSIcons.BookmarkAdd
                        },
                        contentDescription = null,
                        onClick = { onSaveButtonClick(item.value) },
                        modifier = Modifier,
                    )
                }
            }
        }
    }
}

@Composable
@ThemePreviews
private fun PreviewTalkingKotlinCard_unsaved() {
    KSTheme {
        Surface {
            TalkingKotlinCard(
                item = FeedItem.TalkingKotlin(
                    id = "1",
                    title = "Making Multiplatform Better",
                    publishTime = "2023-09-18T22:00:00Z".toInstant(),
                    contentUrl = "contentUrl",
                    savedForLater = false,
                    podcastLogoUrl = "",
                ).toDisplayable("Moments ago"),
                onItemClick = {},
                onSaveButtonClick = {},
                modifier = Modifier.padding(24.dp),
            )
        }
    }
}

@Composable
@ThemePreviews
private fun PreviewTalkingKotlinCard_saved() {
    KSTheme {
        Surface {
            TalkingKotlinCard(
                item = FeedItem.TalkingKotlin(
                    id = "1",
                    title = "Making Multiplatform Better",
                    publishTime = "2023-09-18T22:00:00Z".toInstant(),
                    contentUrl = "contentUrl",
                    savedForLater = true,
                    podcastLogoUrl = "",
                ).toDisplayable("Moments ago"),
                onItemClick = {},
                onSaveButtonClick = {},
                modifier = Modifier.padding(24.dp),
            )
        }
    }
}
