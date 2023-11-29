package io.github.reactivecircus.kstreamlined.android.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.feature.home.component.FeedFilterChip
import io.github.reactivecircus.kstreamlined.android.feature.home.component.SyncButton
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import kotlinx.coroutines.delay
import kotlinx.datetime.toInstant

@Composable
public fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KSTheme.colorScheme.background),
    ) {
        TopNavBar(
            title = stringResource(id = R.string.title_home),
            contentPadding = WindowInsets.statusBars.asPaddingValues(),
            actions = {
                FilledIconButton(
                    KSIcons.Settings,
                    contentDescription = null,
                    onClick = {},
                )
            },
            bottomRow = {
                FeedFilterChip(
                    selectedFeedCount = 4,
                    onClick = {},
                )

                Spacer(modifier = Modifier.width(8.dp))

                var syncing by remember { mutableStateOf(false) }
                LaunchedEffect(syncing) {
                    if (syncing) {
                        @Suppress("MagicNumber")
                        (delay(500))
                        syncing = false
                    }
                }
                SyncButton(
                    onClick = { syncing = true },
                    syncing = syncing,
                )
            }
        )

        @Suppress("MaxLineLength")
        LazyColumn(
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            item {
                Text(
                    text = "This week",
                    style = KSTheme.typography.titleMedium,
                    color = KSTheme.colorScheme.onBackgroundVariant,
                )
            }

            item {
                var item by remember {
                    mutableStateOf(
                        FeedItem.KotlinWeekly(
                            id = "https://mailchi.mp/kotlinweekly/kotlin-weekly-382",
                            title = "Kotlin Weekly #382",
                            publishTime = "2023-11-26T11:14:09Z".toInstant(),
                            contentUrl = "https://mailchi.mp/kotlinweekly/kotlin-weekly-382",
                            savedForLater = false,
                        ).toDisplayable(displayablePublishTime = "Moments ago")
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
                        FeedItem.KotlinYouTube(
                            id = "yt:video:zE2LIAUisRI",
                            title = "Getting Started With KMP: Build Apps for iOS and Android With Shared Logic and Native UIs",
                            publishTime = "2023-11-23T17:00:38Z".toInstant(),
                            contentUrl = "https://www.youtube.com/watch?v=zE2LIAUisRI",
                            savedForLater = false,
                            thumbnailUrl = "https://i3.ytimg.com/vi/zE2LIAUisRI/hqdefault.jpg",
                            description = "During this webinar, we will get you up to speed with the basics of Kotlin Multiplatform. The webinar will cover what's involved in configuring your development environment, creating a Multiplatform Mobile project, and progressing to a more elaborate project that shares the data and networking layers.",
                        ).toDisplayable("2 hours ago"),
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
                        FeedItem.KotlinYouTube(
                            id = "yt:video:bz4cQeaXmsI",
                            title = "The State of Kotlin Multiplatform",
                            publishTime = "2023-11-21T18:47:47Z".toInstant(),
                            contentUrl = "https://www.youtube.com/watch?v=bz4cQeaXmsI",
                            savedForLater = false,
                            thumbnailUrl = "https://i3.ytimg.com/vi/bz4cQeaXmsI/hqdefault.jpg",
                            description = "JetBrains Kotlin Multiplatform (KMP) is an open-source technology designed for flexible cross-platform development. It allows you to develop apps for Android, iOS, desktop, web, and server-side and efficiently reuse code across them, all while retaining the benefits of native programming. After 8 years of development, KMP has been refined into a production-ready technology and is going Stable, which means now is a great time to start using it in your project.",
                        ).toDisplayable("Yesterday"),
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
                            savedForLater = false,
                        ).toDisplayable(displayablePublishTime = "4 days ago")
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
                            savedForLater = false,
                            featuredImageUrl = "https://blog.jetbrains.com/wp-content/uploads/2023/11/kmp_roadmap_720.png",
                        ).toDisplayable(displayablePublishTime = "6 days ago")
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
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Last week",
                    style = KSTheme.typography.titleMedium,
                    color = KSTheme.colorScheme.onBackgroundVariant,
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
                            savedForLater = false,
                            podcastLogoUrl = "https://talkingkotlin.com/images/kotlin_talking_logo.png",
                            tags = listOf(
                                "Kotlin",
                                "KMP",
                                "Kotlin Multiplatform",
                                "http4k",
                            ),
                        ).toDisplayable(displayablePublishTime = "14 Nov 2023")
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
                            savedForLater = false,
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
                            id = "https://blog.jetbrains.com/?post_type=kotlin&p=403389",
                            title = "Welcome Fleet with Kotlin Multiplatform Tooling",
                            publishTime = "2023-11-07T14:24:34Z".toInstant(),
                            contentUrl = "https://blog.jetbrains.com/kotlin/2023/11/kotlin-multiplatform-tooling-in-fleet/",
                            savedForLater = false,
                            featuredImageUrl = "https://blog.jetbrains.com/wp-content/uploads/2023/11/Blog-Featured-1280x720-2x.png",
                        ).toDisplayable(displayablePublishTime = "07 Nov 2023")
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
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Earlier",
                    style = KSTheme.typography.titleMedium,
                    color = KSTheme.colorScheme.onBackgroundVariant,
                )
            }

            item {
                var item by remember {
                    mutableStateOf(
                        FeedItem.KotlinWeekly(
                            id = "https://mailchi.mp/kotlinweekly/kotlin-weekly-379",
                            title = "Kotlin Weekly #379",
                            publishTime = "2023-11-05T08:13:58Z".toInstant(),
                            contentUrl = "https://mailchi.mp/kotlinweekly/kotlin-weekly-379",
                            savedForLater = false,
                        ).toDisplayable(displayablePublishTime = "05 Nov 2023")
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
                            id = "https://blog.jetbrains.com/?post_type=kotlin&p=401216",
                            title = "Compose Multiplatform 1.5.10 – The Perfect Time To Get Started",
                            publishTime = "2023-11-02T12:01:27Z".toInstant(),
                            contentUrl = "https://blog.jetbrains.com/kotlin/2023/11/compose-multiplatform-1-5-10-release/",
                            savedForLater = false,
                            featuredImageUrl = "https://blog.jetbrains.com/wp-content/uploads/2023/11/compose-featured_blog_1280x720.png",
                        ).toDisplayable(displayablePublishTime = "02 Nov 2023")
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
                        FeedItem.KotlinYouTube(
                            id = "yt:video:Ol_96CHKqg8",
                            title = "What's new in Kotlin 1.9.20",
                            publishTime = "2023-11-01T12:44:00Z".toInstant(),
                            contentUrl = "https://www.youtube.com/watch?v=Ol_96CHKqg8",
                            savedForLater = false,
                            thumbnailUrl = "https://i4.ytimg.com/vi/Ol_96CHKqg8/hqdefault.jpg",
                            description = "The Kotlin 1.9.20 release is out, and the K2 compiler for all the targets is now in Beta.",
                        ).toDisplayable("Yesterday"),
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
                        FeedItem.KotlinBlog(
                            id = "https://blog.jetbrains.com/?post_type=kotlin&p=401121",
                            title = "Kotlin Multiplatform Is Stable and Production-Ready",
                            publishTime = "2023-11-01T11:31:28Z".toInstant(),
                            contentUrl = "https://blog.jetbrains.com/kotlin/2023/11/kotlin-multiplatform-stable/",
                            savedForLater = false,
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

            item {
                var item by remember {
                    mutableStateOf(
                        FeedItem.TalkingKotlin(
                            id = "https://talkingkotlin.com/Compose-Multiplatform-in-Production-at-Instabee",
                            title = "Compose Multiplatform in Production on iOS at Instabee",
                            publishTime = "2023-08-09T22:00:00Z".toInstant(),
                            contentUrl = "https://talkingkotlin.com/Compose-Multiplatform-in-Production-at-Instabee/",
                            savedForLater = false,
                            podcastLogoUrl = "https://talkingkotlin.com/images/kotlin_talking_logo.png",
                            tags = listOf(
                                "Kotlin",
                                "Compose",
                                "Compose Multiplatform",
                                "Kotlin Multiplatform",
                            ),
                        ).toDisplayable(displayablePublishTime = "09 Aug 2023")
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
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = KSTheme.colorScheme.container,
                ) {}
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = KSTheme.colorScheme.container,
                ) {}
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = KSTheme.colorScheme.container,
                ) {}
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = KSTheme.colorScheme.onBackgroundVariant,
                ) {}
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = KSTheme.colorScheme.primary,
                ) {}
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = KSTheme.colorScheme.tertiary,
                ) {}
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = KSTheme.colorScheme.secondary,
                ) {}
            }
        }
    }
}
