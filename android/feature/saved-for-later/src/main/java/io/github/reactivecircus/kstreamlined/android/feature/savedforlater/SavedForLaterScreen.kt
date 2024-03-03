package io.github.reactivecircus.kstreamlined.android.feature.savedforlater

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.reactivecircus.kstreamlined.android.foundation.commonui.feed.KotlinBlogCard
import io.github.reactivecircus.kstreamlined.android.foundation.commonui.feed.KotlinWeeklyCard
import io.github.reactivecircus.kstreamlined.android.foundation.commonui.feed.KotlinYouTubeCard
import io.github.reactivecircus.kstreamlined.android.foundation.commonui.feed.TalkingKotlinCard
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.FilledIconButton
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.toDisplayable
import kotlinx.datetime.toInstant
import io.github.reactivecircus.kstreamlined.android.feature.common.R as commonR

@Composable
public fun SavedForLaterScreen(
    onViewItem: (FeedItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KSTheme.colorScheme.background),
    ) {
        TopNavBar(
            title = stringResource(id = R.string.title_saved_for_later),
            contentPadding = WindowInsets.statusBars.asPaddingValues(),
            actions = {
                FilledIconButton(
                    KSIcons.Settings,
                    contentDescription = null,
                    onClick = {},
                )
            },
        )
        if (true) {
            ContentUi(
                onItemClick = onViewItem,
            )
        } else {
            EmptyUi()
        }
    }
}

@Suppress("MaxLineLength")
@Composable
private fun ContentUi(
    onItemClick: (FeedItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = ListContentPadding,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item {
            var item by remember {
                mutableStateOf(
                    FeedItem.KotlinYouTube(
                        id = "yt:video:bz4cQeaXmsI",
                        title = "The State of Kotlin Multiplatform",
                        publishTime = "2023-11-21T18:47:47Z".toInstant(),
                        contentUrl = "https://www.youtube.com/watch?v=bz4cQeaXmsI",
                        savedForLater = true,
                        thumbnailUrl = "https://i3.ytimg.com/vi/bz4cQeaXmsI/hqdefault.jpg",
                        description = "JetBrains Kotlin Multiplatform (KMP) is an open-source technology designed for flexible cross-platform development. It allows you to develop apps for Android, iOS, desktop, web, and server-side and efficiently reuse code across them, all while retaining the benefits of native programming. After 8 years of development, KMP has been refined into a production-ready technology and is going Stable, which means now is a great time to start using it in your project.",
                    ).toDisplayable("21 Nov 2023"),
                )
            }
            KotlinYouTubeCard(
                item = item,
                onItemClick = onItemClick,
                onSaveButtonClick = {
                    item = item.copy(
                        value = it.copy(
                            savedForLater = !it.savedForLater
                        )
                    )
                },
            )
        }

        item {
            var item by remember {
                mutableStateOf(
                    FeedItem.KotlinWeekly(
                        id = "https://mailchi.mp/kotlinweekly/kotlin-weekly-381",
                        title = "Kotlin Weekly #381",
                        publishTime = "2023-11-19T09:13:00Z".toInstant(),
                        contentUrl = "https://mailchi.mp/kotlinweekly/kotlin-weekly-381",
                        savedForLater = true,
                        issueNumber = 381,
                    ).toDisplayable(displayablePublishTime = "19 Nov 2023")
                )
            }
            KotlinWeeklyCard(
                item = item,
                onItemClick = onItemClick,
                onSaveButtonClick = {
                    item = item.copy(
                        value = it.copy(
                            savedForLater = !it.savedForLater
                        )
                    )
                },
            )
        }

        item {
            var item by remember {
                mutableStateOf(
                    FeedItem.KotlinBlog(
                        id = "https://blog.jetbrains.com/?post_type=kotlin&p=405553",
                        title = "Kotlin Multiplatform Development Roadmap for 2024",
                        publishTime = "2023-11-16T11:59:46Z".toInstant(),
                        contentUrl = "https://blog.jetbrains.com/kotlin/2023/11/kotlin-multiplatform-development-roadmap-for-2024/",
                        savedForLater = true,
                        featuredImageUrl = "https://blog.jetbrains.com/wp-content/uploads/2023/11/kmp_roadmap_720.png",
                    ).toDisplayable(displayablePublishTime = "16 Nov 2023")
                )
            }
            KotlinBlogCard(
                item = item,
                onItemClick = onItemClick,
                onSaveButtonClick = {
                    item = item.copy(
                        value = it.copy(
                            savedForLater = !it.savedForLater
                        )
                    )
                },
            )
        }

        item {
            var item by remember {
                mutableStateOf(
                    FeedItem.TalkingKotlin(
                        id = "tag:soundcloud,2010:tracks/1664789634",
                        title = "http4k Chronicles",
                        publishTime = "2023-11-13T23:00:00Z".toInstant(),
                        contentUrl = "https://soundcloud.com/user-38099918/http4k-chronicles",
                        savedForLater = false,
                        audioUrl = "https://feeds.soundcloud.com/stream/1664789634-user-38099918-http4k-chronicles.mp3",
                        thumbnailUrl = "https://i1.sndcdn.com/artworks-uP9Cxy5KSYNzGebf-3q1MsQ-t3000x3000.jpg",
                        summary = "Dive into the intricate world of microservices with the latest episode of Talking Kotlin, hosted by Sebastian and Hadi.",
                        duration = "56min.",
                        startPositionMillis = 0,
                    ).toDisplayable(displayablePublishTime = "13 Nov 2023")
                )
            }
            TalkingKotlinCard(
                item = item,
                onItemClick = onItemClick,
                onSaveButtonClick = {
                    item = item.copy(
                        value = it.copy(
                            savedForLater = !it.savedForLater
                        )
                    )
                },
            )
        }

        item {
            var item by remember {
                mutableStateOf(
                    FeedItem.KotlinBlog(
                        id = "https://blog.jetbrains.com/?post_type=blog&p=404245",
                        title = "Amper – Improving the Build Tooling User Experience",
                        publishTime = "2023-11-09T10:07:24Z".toInstant(),
                        contentUrl = "https://blog.jetbrains.com/blog/2023/11/09/amper-improving-the-build-tooling-user-experience/",
                        savedForLater = true,
                        featuredImageUrl = "https://blog.jetbrains.com/wp-content/uploads/2023/11/Blog-Featured-1280x720-2x-1-2.png",
                    ).toDisplayable(displayablePublishTime = "09 Nov 2023")
                )
            }
            KotlinBlogCard(
                item = item,
                onItemClick = onItemClick,
                onSaveButtonClick = {
                    item = item.copy(
                        value = it.copy(
                            savedForLater = !it.savedForLater
                        )
                    )
                },
            )
        }

        item {
            var item by remember {
                mutableStateOf(
                    FeedItem.KotlinBlog(
                        id = "https://blog.jetbrains.com/?post_type=kotlin&p=401121",
                        title = "Kotlin Multiplatform Is Stable and Production-Ready",
                        publishTime = "2023-11-01T11:31:28Z".toInstant(),
                        contentUrl = "https://blog.jetbrains.com/kotlin/2023/11/kotlin-multiplatform-stable/",
                        savedForLater = true,
                        featuredImageUrl = "https://blog.jetbrains.com/wp-content/uploads/2023/11/DSGN-17931-Banners-for-1.9.20-release-and-KMP-Stable-annoucement_Blog-Social-share-image-1280x720-1-1.png",
                    ).toDisplayable(displayablePublishTime = "01 Nov 2023")
                )
            }
            KotlinBlogCard(
                item = item,
                onItemClick = onItemClick,
                onSaveButtonClick = {
                    item = item.copy(
                        value = it.copy(
                            savedForLater = !it.savedForLater
                        )
                    )
                },
            )
        }
    }
}

@Composable
private fun EmptyUi(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(ListContentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AsyncImage(
            commonR.drawable.ic_kodee_lost,
            contentDescription = null,
            modifier = Modifier.size(160.dp),
        )
        Spacer(modifier = Modifier.height(36.dp))
        Text(
            text = stringResource(id = commonR.string.empty_state_message),
            style = KSTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = TextAlign.Center,
        )
    }
}

private val ListContentPadding = PaddingValues(
    top = 24.dp,
    start = 24.dp,
    end = 24.dp,
    bottom = 120.dp,
)
