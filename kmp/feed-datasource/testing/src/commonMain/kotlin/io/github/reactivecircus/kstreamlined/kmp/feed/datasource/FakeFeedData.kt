@file:Suppress("MaximumLineLength", "MaxLineLength")

package io.github.reactivecircus.kstreamlined.kmp.feed.datasource

import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model.FeedOrigin

public val FakeFeedOrigins: List<FeedOrigin> = listOf(
    FeedOrigin(
        key = FeedOrigin.Key.KotlinBlog,
        title = "Kotlin Blog",
        description = "Latest news from the official Kotlin Blog",
    ),
    FeedOrigin(
        key = FeedOrigin.Key.KotlinYouTubeChannel,
        title = "Kotlin YouTube",
        description = "The official YouTube channel of the Kotlin programming language",
    ),
    FeedOrigin(
        key = FeedOrigin.Key.TalkingKotlinPodcast,
        title = "Talking Kotlin",
        description = "Technical show discussing everything Kotlin, hosted by Hadi and Sebastian",
    ),
    FeedOrigin(
        key = FeedOrigin.Key.KotlinWeekly,
        title = "Kotlin Weekly",
        description = "Weekly community Kotlin newsletter, hosted by Enrique",
    ),
)

public val FakeFeedEntries: List<FeedEntry> = listOf(
    FeedEntry.KotlinBlog(
        id = "https://blog.jetbrains.com/?post_type=kotlin&p=405553",
        title = "Kotlin Multiplatform Development Roadmap for 2024",
        publishTime = "2023-11-16T11:59:46Z",
        contentUrl = "https://blog.jetbrains.com/kotlin/2023/11/kotlin-multiplatform-development-roadmap-for-2024/",
        featuredImageUrl = "https://blog.jetbrains.com/wp-content/uploads/2023/11/kmp_roadmap_720.png",
        description = "With the recently achieved stability of Kotlin Multiplatform, development teams worldwide can now seamlessly and confidently adopt it in production. However, this is just the beginning for KMP and its ecosystem. To equip you with the best cross-platform development experience, JetBrains aims to deliver a host of further improvements to the core Kotlin Multiplatform technology, […]",
    ),
    FeedEntry.KotlinYouTube(
        id = "yt:video:bz4cQeaXmsI",
        title = "The State of Kotlin Multiplatform",
        publishTime = "2023-11-21T18:47:47Z",
        contentUrl = "https://www.youtube.com/watch?v=bz4cQeaXmsI",
        thumbnailUrl = "https://i3.ytimg.com/vi/bz4cQeaXmsI/hqdefault.jpg",
        description = "JetBrains Kotlin Multiplatform (KMP) is an open-source technology designed for flexible cross-platform development. It allows you to develop apps for Android, iOS, desktop, web, and server-side and efficiently reuse code across them, all while retaining the benefits of native programming. After 8 years of development, KMP has been refined into a production-ready technology and is going Stable, which means now is a great time to start using it in your project.",
    ),
    FeedEntry.TalkingKotlin(
        id = "https://talkingkotlin.com/making-multiplatform-better",
        title = "Making Multiplatform Better",
        publishTime = "Making Multiplatform Better",
        contentUrl = "https://talkingkotlin.com/making-multiplatform-better/",
        podcastLogoUrl = "https://talkingkotlin.com/images/kotlin_talking_logo.png",
        tags = listOf(
            "Kotlin",
            "KMP",
            "Kotlin Multiplatform",
            "Coroutines",
        ),
    ),
    FeedEntry.KotlinWeekly(
        id = "https://mailchi.mp/kotlinweekly/kotlin-weekly-381",
        title = "Kotlin Weekly #381",
        publishTime = "2023-11-19T09:13:00Z",
        contentUrl = "https://mailchi.mp/kotlinweekly/kotlin-weekly-381",
    ),
)
