@file:Suppress("MaximumLineLength", "MaxLineLength")

package io.github.reactivecircus.kstreamlined.kmp.feed.datasource

import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedSource
import kotlinx.datetime.toInstant

public val FakeFeedSources: List<FeedSource> = listOf(
    FeedSource(
        key = FeedSource.Key.KotlinBlog,
        title = "Kotlin Blog",
        description = "Latest news from the official Kotlin Blog",
    ),
    FeedSource(
        key = FeedSource.Key.KotlinYouTubeChannel,
        title = "Kotlin YouTube",
        description = "The official YouTube channel of the Kotlin programming language",
    ),
    FeedSource(
        key = FeedSource.Key.TalkingKotlinPodcast,
        title = "Talking Kotlin",
        description = "Technical show discussing everything Kotlin, hosted by Hadi and Sebastian",
    ),
    FeedSource(
        key = FeedSource.Key.KotlinWeekly,
        title = "Kotlin Weekly",
        description = "Weekly community Kotlin newsletter, hosted by Enrique",
    ),
)

public val FakeFeedEntries: List<FeedEntry> = listOf(
    FeedEntry.KotlinBlog(
        id = "https://blog.jetbrains.com/?post_type=kotlin&p=405553",
        title = "Kotlin Multiplatform Development Roadmap for 2024",
        publishTime = "2023-11-16T11:59:46Z".toInstant(),
        contentUrl = "https://blog.jetbrains.com/kotlin/2023/11/kotlin-multiplatform-development-roadmap-for-2024/",
        featuredImageUrl = "https://blog.jetbrains.com/wp-content/uploads/2023/11/kmp_roadmap_720.png",
    ),
    FeedEntry.KotlinYouTube(
        id = "yt:video:bz4cQeaXmsI",
        title = "The State of Kotlin Multiplatform",
        publishTime = "2023-11-21T18:47:47Z".toInstant(),
        contentUrl = "https://www.youtube.com/watch?v=bz4cQeaXmsI",
        thumbnailUrl = "https://i3.ytimg.com/vi/bz4cQeaXmsI/hqdefault.jpg",
        description = "JetBrains Kotlin Multiplatform (KMP) is an open-source technology designed for flexible cross-platform development. It allows you to develop apps for Android, iOS, desktop, web, and server-side and efficiently reuse code across them, all while retaining the benefits of native programming. After 8 years of development, KMP has been refined into a production-ready technology and is going Stable, which means now is a great time to start using it in your project.",
    ),
    FeedEntry.TalkingKotlin(
        id = "tag:soundcloud,2010:tracks/1620132927",
        title = "Making Multiplatform Better",
        publishTime = "2023-09-18T22:00:00Z".toInstant(),
        contentUrl = "https://soundcloud.com/user-38099918/making-multiplatform-better",
        thumbnailUrl = "https://i1.sndcdn.com/artworks-zzYOFetx0rxBg1eJ-8bJzmw-t3000x3000.jpg",
        summary = "In this episode, we talk to Kevin Galligan and JP Cathcart from Touchlab about the Kotlin Multiplatform ecosystem and the new Kotlin Multiplatform Mobile (KMM) plugin for Android Studio. We discuss the state of Kotlin Multiplatform, the challenges of building cross-platform libraries, and how KMM helps to address these challenges. We also touch on the new KMM plugin for Android Studio and how it helps to streamline the development of cross-platform apps.",
        duration = "45min.",
    ),
    FeedEntry.KotlinWeekly(
        id = "https://mailchi.mp/kotlinweekly/kotlin-weekly-381",
        title = "Kotlin Weekly #381",
        publishTime = "2023-11-19T09:13:00Z".toInstant(),
        contentUrl = "https://mailchi.mp/kotlinweekly/kotlin-weekly-381",
    ),
)
