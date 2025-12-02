@file:OptIn(ExperimentalSharedTransitionApi::class)

package io.github.reactivecircus.kstreamlined.android.core.commonui.feed

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.tracing.trace
import coil3.compose.AsyncImage
import io.github.reactivecircus.kstreamlined.android.core.composeutils.marqueeWithFadedEdges
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Icon
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
public fun SharedTransitionScope.KotlinYouTubeCard(
    item: DisplayableFeedItem<FeedItem.KotlinYouTube>,
    onItemClick: (FeedItem.KotlinYouTube) -> Unit,
    onSaveButtonClick: (FeedItem.KotlinYouTube) -> Unit,
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    saveButtonElementKey: String? = null,
): Unit = trace("FeedItem:KotlinYouTubeCard") {
    Surface(
        onClick = { onItemClick(item.value) },
        modifier = modifier
            .fillMaxWidth()
            .testTag("kotlinYouTubeCard"),
        shape = RoundedCornerShape(16.dp),
        color = KSTheme.colorScheme.secondary,
        contentColor = KSTheme.colorScheme.onSecondary,
    ) {
        Column {
            Box(contentAlignment = Alignment.Center) {
                AsyncImage(
                    model = item.value.thumbnailUrl,
                    contentDescription = item.value.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(ImageHeight)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.FillWidth,
                )
                PlayIconOverlay()
            }

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
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    modifier = Modifier
                        .marqueeWithFadedEdges(
                            edgeWidth = 8.dp,
                            iterations = 1,
                            velocity = 80.dp,
                        ),
                    text = item.value.description,
                    style = KSTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.displayablePublishTime,
                        style = KSTheme.typography.bodyMedium,
                        color = KSTheme.colorScheme.onSecondaryVariant,
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
                        modifier = Modifier
                            .testTag("saveButton")
                            .then(
                                if (animatedVisibilityScope != null && saveButtonElementKey != null) {
                                    Modifier.sharedElement(
                                        sharedContentState = rememberSharedContentState(key = saveButtonElementKey),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        zIndexInOverlay = 1f,
                                    )
                                } else {
                                    Modifier
                                },
                            ),
                    )
                }
            }
        }
    }
}

@Composable
private fun PlayIconOverlay(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.2f)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = KSIcons.PlayArrow,
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .size(40.dp),
        )
    }
}

private val ImageHeight = 200.dp

@Composable
@PreviewLightDark
private fun PreviewKotlinYouTubeCard_unsaved() {
    KSTheme {
        Surface {
            SharedTransitionLayout {
                KotlinYouTubeCard(
                    item = FeedItem.KotlinYouTube(
                        id = "1",
                        title = "The State of Kotlin Multiplatform",
                        publishTime = Instant.parse("2023-11-21T18:47:47Z"),
                        contentUrl = "contentUrl",
                        savedForLater = false,
                        thumbnailUrl = "",
                        description = "JetBrains Kotlin Multiplatform (KMP) is an open-source technology",
                    ).toDisplayable("3 days ago"),
                    onItemClick = {},
                    onSaveButtonClick = {},
                    modifier = Modifier.padding(24.dp),
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewKotlinYouTubeCard_saved() {
    KSTheme {
        Surface {
            SharedTransitionLayout {
                KotlinYouTubeCard(
                    item = FeedItem.KotlinYouTube(
                        id = "1",
                        title = "The State of Kotlin Multiplatform",
                        publishTime = Instant.parse("2023-11-21T18:47:47Z"),
                        contentUrl = "contentUrl",
                        savedForLater = true,
                        thumbnailUrl = "",
                        description = "JetBrains Kotlin Multiplatform (KMP) is an open-source technology",
                    ).toDisplayable("3 days ago"),
                    onItemClick = {},
                    onSaveButtonClick = {},
                    modifier = Modifier.padding(24.dp),
                )
            }
        }
    }
}
