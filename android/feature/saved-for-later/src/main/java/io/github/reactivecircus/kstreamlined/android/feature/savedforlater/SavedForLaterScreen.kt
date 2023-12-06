package io.github.reactivecircus.kstreamlined.android.feature.savedforlater

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.common.ui.feed.KotlinBlogCard
import io.github.reactivecircus.kstreamlined.android.common.ui.feed.KotlinWeeklyCard
import io.github.reactivecircus.kstreamlined.android.common.ui.feed.KotlinYouTubeCard
import io.github.reactivecircus.kstreamlined.android.common.ui.feed.TalkingKotlinCard
import io.github.reactivecircus.kstreamlined.android.common.ui.feed.toDisplayable
import io.github.reactivecircus.kstreamlined.android.designsystem.component.FilledIconButton
import io.github.reactivecircus.kstreamlined.android.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import kotlinx.datetime.toInstant

@Composable
public fun SavedForLaterScreen(
    modifier: Modifier = Modifier
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

        @Suppress("MaxLineLength")
        LazyColumn(
            contentPadding = PaddingValues(24.dp),
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
                    onItemClick = {},
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
                        ).toDisplayable(displayablePublishTime = "19 Nov 2023")
                    )
                }
                KotlinWeeklyCard(
                    item = item,
                    onItemClick = {},
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
                    onItemClick = {},
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
                            id = "https://talkingkotlin.com/http4k-chronicles",
                            title = "http4k Chronicles",
                            publishTime = "2023-11-13T23:00:00Z".toInstant(),
                            contentUrl = "https://talkingkotlin.com/http4k-chronicles/",
                            savedForLater = true,
                            podcastLogoUrl = "https://talkingkotlin.com/images/kotlin_talking_logo.png",
                        ).toDisplayable(displayablePublishTime = "13 Nov 2023")
                    )
                }
                TalkingKotlinCard(
                    item = item,
                    onItemClick = {},
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
                    onItemClick = {},
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
                    onItemClick = {},
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
}
