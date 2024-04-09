package io.github.reactivecircus.kstreamlined.android.foundation.commonui.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.tracing.trace
import coil3.compose.AsyncImage
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.IconButton
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.BookmarkAdd
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.BookmarkFill
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.kmp.model.feed.DisplayableFeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.toDisplayable
import kotlinx.datetime.toInstant

@Composable
public fun KotlinBlogCard(
    item: DisplayableFeedItem<FeedItem.KotlinBlog>,
    onItemClick: (FeedItem.KotlinBlog) -> Unit,
    onSaveButtonClick: (FeedItem.KotlinBlog) -> Unit,
    modifier: Modifier = Modifier,
): Unit = trace("FeedItem:KotlinBlogCard") {
    Surface(
        onClick = { onItemClick(item.value) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = KSTheme.colorScheme.container,
        contentColor = KSTheme.colorScheme.onBackground,
    ) {
        Column {
            AsyncImage(
                model = item.value.featuredImageUrl,
                contentDescription = item.value.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(ImageHeight),
                contentScale = ContentScale.FillWidth,
            )

            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 8.dp,
                    top = 24.dp,
                    bottom = 8.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = item.value.title,
                    style = KSTheme.typography.titleMedium,
                    modifier = Modifier.padding(end = 8.dp),
                    overflow = TextOverflow.Ellipsis,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.displayablePublishTime,
                        style = KSTheme.typography.bodyMedium,
                        color = KSTheme.colorScheme.onBackgroundVariant,
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
                        modifier = Modifier.testTag("saveButton"),
                    )
                }
            }
        }
    }
}

private val ImageHeight = 200.dp

@Composable
@PreviewLightDark
private fun PreviewKotlinBlogCard_unsaved() {
    KSTheme {
        Surface {
            KotlinBlogCard(
                item = FeedItem.KotlinBlog(
                    id = "1",
                    title = "Kotlin Multiplatform Development Roadmap for 2024",
                    publishTime = "2023-11-16T11:59:46Z".toInstant(),
                    contentUrl = "contentUrl",
                    savedForLater = false,
                    featuredImageUrl = "",
                ).toDisplayable("Moments ago"),
                onItemClick = {},
                onSaveButtonClick = {},
                modifier = Modifier.padding(24.dp),
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewKotlinBlogCard_saved() {
    KSTheme {
        Surface {
            KotlinBlogCard(
                item = FeedItem.KotlinBlog(
                    id = "1",
                    title = "Kotlin Multiplatform Development Roadmap for 2024",
                    publishTime = "2023-11-16T11:59:46Z".toInstant(),
                    contentUrl = "contentUrl",
                    savedForLater = true,
                    featuredImageUrl = "",
                ).toDisplayable("Moments ago"),
                onItemClick = {},
                onSaveButtonClick = {},
                modifier = Modifier.padding(24.dp),
            )
        }
    }
}
