package io.github.reactivecircus.kstreamlined.android.core.ui.feed

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SharedTransitionScope.ResizeMode.Companion.scaleToBounds
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.fillWidth
import androidx.compose.foundation.style.styleable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tracing.trace
import coil3.compose.AsyncImage
import io.github.reactivecircus.kstreamlined.android.core.animation.NoOpSharedElementLayout
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.IconButton
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.core.designsystem.preview.PreviewKStreamlined
import io.github.reactivecircus.kstreamlined.kmp.feed.model.DisplayableFeedItem
import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.feed.model.toDisplayable
import kotlin.time.Instant

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
context(sharedTransitionScope: SharedTransitionScope, animatedVisibilityScope: AnimatedVisibilityScope)
public fun KotlinBlogCard(
    item: DisplayableFeedItem<FeedItem.KotlinBlog>,
    onItemClick: (FeedItem.KotlinBlog) -> Unit,
    onSaveButtonClick: (FeedItem.KotlinBlog) -> Unit,
    onTldrClick: (FeedItem.KotlinBlog) -> Unit,
    modifier: Modifier = Modifier,
    saveButtonBoundsKey: String? = null,
    tldrButtonBoundsKey: String? = null,
    titleBoundsKey: String? = null,
): Unit = trace("FeedItem:KotlinBlogCard") {
    Surface(
        onClick = { onItemClick(item.value) },
        modifier = modifier
            .fillMaxWidth()
            .testTag("kotlinBlogCard"),
        shape = RoundedCornerShape(16.dp),
        color = KSTheme.colorScheme.surface,
        contentColor = KSTheme.colorScheme.onBackground,
    ) {
        Column {
            AsyncImage(
                model = item.value.featuredImageUrl,
                contentDescription = item.value.title,
                modifier = Modifier.styleable {
                    fillWidth()
                    height(ImageHeight)
                },
                contentScale = ContentScale.FillWidth,
            )

            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 8.dp,
                    top = if (item.value.supportsTldr) 16.dp else 24.dp,
                    bottom = 8.dp,
                ),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                if (item.value.supportsTldr) {
                    TldrButton(
                        onClick = { onTldrClick(item.value) },
                        modifier = if (tldrButtonBoundsKey != null) {
                            with(sharedTransitionScope) {
                                Modifier.sharedBounds(
                                    sharedContentState = rememberSharedContentState(key = tldrButtonBoundsKey),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    zIndexInOverlay = 1f,
                                )
                            }
                        } else {
                            Modifier
                        },
                    )
                }
                Text(
                    text = item.value.title,
                    style = KSTheme.typography.titleMedium,
                    modifier = Modifier
                        .then(
                            if (titleBoundsKey != null) {
                                with(sharedTransitionScope) {
                                    Modifier.sharedBounds(
                                        sharedContentState = rememberSharedContentState(key = titleBoundsKey),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        zIndexInOverlay = 1f,
                                        resizeMode = scaleToBounds(ContentScale.FillWidth, Alignment.TopStart),
                                    )
                                }
                            } else {
                                Modifier
                            },
                        ),
                    overflow = TextOverflow.Ellipsis,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.displayablePublishTime,
                        style = KSTheme.typography.bodyMedium,
                        color = KSTheme.colorScheme.onBackgroundMuted,
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
                                if (saveButtonBoundsKey != null) {
                                    with(sharedTransitionScope) {
                                        Modifier.sharedBounds(
                                            sharedContentState = rememberSharedContentState(key = saveButtonBoundsKey),
                                            animatedVisibilityScope = animatedVisibilityScope,
                                            zIndexInOverlay = 1f,
                                        )
                                    }
                                } else {
                                    Modifier
                                },
                            ),
                        iconTint = KSTheme.colorScheme.onBackgroundMuted,
                    )
                }
            }
        }
    }
}

private val ImageHeight = 200.dp

@Composable
private fun TldrButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .testTag("tldrButton")
            .semantics { role = Role.Button },
        shape = CircleShape,
        color = KSTheme.colorScheme.surfaceAccent,
        contentColor = KSTheme.colorScheme.onSurfaceAccent,
    ) {
        Row(
            modifier = Modifier
                .padding(start = 8.dp, end = 10.dp)
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                KSIcons.Sparkles,
                contentDescription = null,
                modifier = Modifier.requiredSize(20.dp),
            )
            Text(
                text = stringResource(R.string.tldr),
                style = KSTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.1.sp,
                ),
            )
        }
    }
}

@Composable
@PreviewKStreamlined
private fun PreviewKotlinBlogCard_unsaved() {
    NoOpSharedElementLayout {
        KotlinBlogCard(
            item = FeedItem.KotlinBlog(
                id = "1",
                title = "Kotlin Multiplatform Development Roadmap for 2024",
                publishTime = Instant.parse("2023-11-16T11:59:46Z"),
                contentUrl = "contentUrl",
                savedForLater = false,
                featuredImageUrl = "",
                supportsTldr = true,
            ).toDisplayable("Moments ago"),
            onItemClick = {},
            onSaveButtonClick = {},
            onTldrClick = {},
            modifier = Modifier.padding(24.dp),
        )
    }
}

@Composable
@PreviewKStreamlined
private fun PreviewKotlinBlogCard_saved() {
    NoOpSharedElementLayout {
        KotlinBlogCard(
            item = FeedItem.KotlinBlog(
                id = "1",
                title = "Kotlin Multiplatform Development Roadmap for 2024",
                publishTime = Instant.parse("2023-11-16T11:59:46Z"),
                contentUrl = "contentUrl",
                savedForLater = true,
                featuredImageUrl = "",
                supportsTldr = true,
            ).toDisplayable("Moments ago"),
            onItemClick = {},
            onSaveButtonClick = {},
            onTldrClick = {},
            modifier = Modifier.padding(24.dp),
        )
    }
}

@Composable
@PreviewKStreamlined
private fun PreviewKotlinBlogCard_noTldrSummary() {
    NoOpSharedElementLayout {
        KotlinBlogCard(
            item = FeedItem.KotlinBlog(
                id = "1",
                title = "Kotlin Multiplatform Development Roadmap for 2024",
                publishTime = Instant.parse("2023-11-16T11:59:46Z"),
                contentUrl = "contentUrl",
                savedForLater = false,
                featuredImageUrl = "",
                supportsTldr = false,
            ).toDisplayable("Moments ago"),
            onItemClick = {},
            onSaveButtonClick = {},
            modifier = Modifier.padding(24.dp),
            onTldrClick = {},
        )
    }
}
