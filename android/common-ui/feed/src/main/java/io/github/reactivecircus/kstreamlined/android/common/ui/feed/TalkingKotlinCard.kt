package io.github.reactivecircus.kstreamlined.android.common.ui.feed

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import io.github.reactivecircus.kstreamlined.android.designsystem.component.IconButton
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.BookmarkAdd
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.BookmarkFill
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.kmp.model.feed.DisplayableFeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.toDisplayable
import kotlinx.datetime.toInstant

@OptIn(ExperimentalFoundationApi::class)
@Composable
public fun TalkingKotlinCard(
    item: DisplayableFeedItem<FeedItem.TalkingKotlin>,
    onItemClick: (FeedItem.TalkingKotlin) -> Unit,
    onSaveButtonClick: (FeedItem.TalkingKotlin) -> Unit,
    modifier: Modifier = Modifier,
) {
    val brush = Brush.horizontalGradient(
        colors = listOf(
            KSTheme.colorScheme.tertiary,
            KSTheme.colorScheme.tertiaryVariant,
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
            .fillMaxWidth(),
        color = Color.Transparent,
        contentColor = KSTheme.colorScheme.onTertiary,
    ) {
        Column(
            modifier = Modifier.padding(
                start = 16.dp,
                end = 8.dp,
                top = 16.dp,
                bottom = 16.dp,
            )
        ) {
            Row {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    var showSummary by remember { mutableStateOf(false) }
                    Text(
                        text = item.displayablePublishTime,
                        style = KSTheme.typography.bodyMedium,
                        color = KSTheme.colorScheme.onTertiaryVariant,
                    )
                    Text(
                        text = item.value.title,
                        style = KSTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            lineHeight = 20.sp,
                        ),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 3,
                        onTextLayout = {
                            showSummary = it.lineCount <= 1
                        },
                    )
                    if (showSummary) {
                        Text(
                            modifier = Modifier
                                .basicMarquee(
                                    iterations = 1,
                                    velocity = 80.dp,
                                ),
                            text = item.value.summary,
                            style = KSTheme.typography.bodyMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                AsyncImage(
                    model = item.value.thumbnailUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(ImageSize)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Fit,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = KSTheme.colorScheme.containerOnTertiary,
                    contentColor = KSTheme.colorScheme.primary,
                ) {
                    Text(
                        text = item.value.duration,
                        style = KSTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                        ),
                        modifier = Modifier.padding(
                            vertical = 8.dp,
                            horizontal = 12.dp,
                        ),
                    )
                }
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

private val ImageSize = 88.dp

@Composable
@PreviewLightDark
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
                    audioUrl = "",
                    thumbnailUrl = "",
                    summary = "In this episode, we talk to Rick Clephas",
                    duration = "45min.",
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
                    audioUrl = "",
                    thumbnailUrl = "",
                    summary = "In this episode, we talk to Rick Clephas.",
                    duration = "1h 3min.",
                ).toDisplayable("Moments ago"),
                onItemClick = {},
                onSaveButtonClick = {},
                modifier = Modifier.padding(24.dp),
            )
        }
    }
}
