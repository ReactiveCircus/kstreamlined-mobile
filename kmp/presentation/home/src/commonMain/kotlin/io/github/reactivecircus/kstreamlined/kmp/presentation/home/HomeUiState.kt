package io.github.reactivecircus.kstreamlined.kmp.presentation.home

import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import kotlinx.datetime.toInstant

// TODO re-model to support pull to refresh and transient error
public sealed interface HomeUiState {
    public data object InFlight : HomeUiState
    public sealed interface Error : HomeUiState {
        public data object Network : Error
        public data object Server : Error
    }
    public data class Content(
        val feedItems: List<HomeFeedItem>,
    ) : HomeUiState
}

@Suppress("MaxLineLength")
public val FakeHomeFeedItems: List<HomeFeedItem> = listOf(
    FeedItem.KotlinYouTube(
        id = "yt:video:apiVJfLvUBE",
        title = "Coil Goes Multiplatform with Colin White | Talking Kotlin #127",
        publishTime = "2023-11-29T17:30:08Z".toInstant(),
        contentUrl = "https://www.youtube.com/watch?v=apiVJfLvUBE",
        savedForLater = false,
        thumbnailUrl = "https://i2.ytimg.com/vi/apiVJfLvUBE/hqdefault.jpg",
        description = "Welcome to another engaging episode of Talking Kotlin! In this edition, we dive into the dynamic world of Android development with Colin White, the creator of the widely acclaimed Coil library. Join us as we discuss the latest developments, insights, and the exciting roadmap for Coil. \uD83D\uDE80 Highlights from this Episode: Learn about Colin's journey in developing the Coil library. Discover the pivotal role Coil plays in simplifying image loading for Android developers. Get an exclusive sneak peek into the upcoming Coil 3.0, featuring multi-platform support and seamless integration with Jetpack Compose. \uD83D\uDD17 Helpful Links: Coil Library GitHub: https://coil-kt.github.io/coil/ Follow Colin White on Twitter: https://twitter.com/colinwhi \uD83C\uDF10 Connect with the Kotlin Community: https://kotlinlang.org/community/ Kotlin Foundation: https://kotlinfoundation.org/ \uD83D\uDC49 Don't miss out on the latest insights and updates from the Kotlin world! Subscribe, hit the bell icon, and join the conversation in the comments below. \uD83D\uDCC8 Help us reach 20,000 views by liking, sharing, and subscribing! Your support keeps the Kotlin conversation alive.",
    ),
    FeedItem.KotlinWeekly(
        id = "https://mailchi.mp/kotlinweekly/kotlin-weekly-382",
        title = "Kotlin Weekly #382",
        publishTime = "2023-11-26T11:14:09Z".toInstant(),
        contentUrl = "https://mailchi.mp/kotlinweekly/kotlin-weekly-382",
        savedForLater = false,
    ),
    FeedItem.KotlinBlog(
        id = "https://blog.jetbrains.com/?post_type=kotlin&p=409489",
        title = "Tackle Advent of Code 2023 With Kotlin and Win Prizes!",
        publishTime = "2023-11-23T17:00:38Z".toInstant(),
        contentUrl = "https://blog.jetbrains.com/kotlin/2023/11/advent-of-code-2023-with-kotlin/",
        savedForLater = false,
        featuredImageUrl = "https://blog.jetbrains.com/wp-content/uploads/2023/11/DSGN-18072-Social-media-banners_Blog-Featured-image-1280x720-1.png",
    ),
    FeedItem.KotlinYouTube(
        id = "yt:video:bz4cQeaXmsI",
        title = "The State of Kotlin Multiplatform",
        publishTime = "2023-11-21T18:47:47Z".toInstant(),
        contentUrl = "https://www.youtube.com/watch?v=bz4cQeaXmsI",
        savedForLater = false,
        thumbnailUrl = "https://i3.ytimg.com/vi/bz4cQeaXmsI/hqdefault.jpg",
        description = "JetBrains Kotlin Multiplatform (KMP) is an open-source technology designed for flexible cross-platform development. It allows you to develop apps for Android, iOS, desktop, web, and server-side and efficiently reuse code across them, all while retaining the benefits of native programming. After 8 years of development, KMP has been refined into a production-ready technology and is going Stable, which means now is a great time to start using it in your project.",
    ),
    FeedItem.KotlinWeekly(
        id = "https://mailchi.mp/kotlinweekly/kotlin-weekly-381",
        title = "Kotlin Weekly #381",
        publishTime = "2023-11-19T09:13:00Z".toInstant(),
        contentUrl = "https://mailchi.mp/kotlinweekly/kotlin-weekly-381",
        savedForLater = false,
    ),
    FeedItem.KotlinBlog(
        id = "https://blog.jetbrains.com/?post_type=kotlin&p=405553",
        title = "Kotlin Multiplatform Development Roadmap for 2024",
        publishTime = "2023-11-16T11:59:46Z".toInstant(),
        contentUrl = "https://blog.jetbrains.com/kotlin/2023/11/kotlin-multiplatform-development-roadmap-for-2024/",
        savedForLater = false,
        featuredImageUrl = "https://blog.jetbrains.com/wp-content/uploads/2023/11/kmp_roadmap_720.png",
    ),
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
    ),
    FeedItem.KotlinBlog(
        id = "https://blog.jetbrains.com/?post_type=blog&p=404245",
        title = "Amper – Improving the Build Tooling User Experience",
        publishTime = "2023-11-09T10:07:24Z".toInstant(),
        contentUrl = "https://blog.jetbrains.com/blog/2023/11/09/amper-improving-the-build-tooling-user-experience/",
        savedForLater = false,
        featuredImageUrl = "https://blog.jetbrains.com/wp-content/uploads/2023/11/Blog-Featured-1280x720-2x-1-2.png",
    ),
    FeedItem.KotlinBlog(
        id = "https://blog.jetbrains.com/?post_type=kotlin&p=403389",
        title = "Welcome Fleet with Kotlin Multiplatform Tooling",
        publishTime = "2023-11-07T14:24:34Z".toInstant(),
        contentUrl = "https://blog.jetbrains.com/kotlin/2023/11/kotlin-multiplatform-tooling-in-fleet/",
        savedForLater = false,
        featuredImageUrl = "https://blog.jetbrains.com/wp-content/uploads/2023/11/Blog-Featured-1280x720-2x.png",
    ),
    FeedItem.KotlinWeekly(
        id = "https://mailchi.mp/kotlinweekly/kotlin-weekly-379",
        title = "Kotlin Weekly #379",
        publishTime = "2023-11-05T08:13:58Z".toInstant(),
        contentUrl = "https://mailchi.mp/kotlinweekly/kotlin-weekly-379",
        savedForLater = false,
    ),
    FeedItem.KotlinBlog(
        id = "https://blog.jetbrains.com/?post_type=kotlin&p=401216",
        title = "Compose Multiplatform 1.5.10 – The Perfect Time To Get Started",
        publishTime = "2023-11-02T12:01:27Z".toInstant(),
        contentUrl = "https://blog.jetbrains.com/kotlin/2023/11/compose-multiplatform-1-5-10-release/",
        savedForLater = false,
        featuredImageUrl = "https://blog.jetbrains.com/wp-content/uploads/2023/11/compose-featured_blog_1280x720.png",
    ),
    FeedItem.KotlinYouTube(
        id = "yt:video:Ol_96CHKqg8",
        title = "What's new in Kotlin 1.9.20",
        publishTime = "2023-11-01T12:44:00Z".toInstant(),
        contentUrl = "https://www.youtube.com/watch?v=Ol_96CHKqg8",
        savedForLater = false,
        thumbnailUrl = "https://i4.ytimg.com/vi/Ol_96CHKqg8/hqdefault.jpg",
        description = "The Kotlin 1.9.20 release is out, and the K2 compiler for all the targets is now in Beta.",
    ),
    FeedItem.KotlinBlog(
        id = "https://blog.jetbrains.com/?post_type=kotlin&p=401121",
        title = "Kotlin Multiplatform Is Stable and Production-Ready",
        publishTime = "2023-11-01T11:31:28Z".toInstant(),
        contentUrl = "https://blog.jetbrains.com/kotlin/2023/11/kotlin-multiplatform-stable/",
        savedForLater = false,
        featuredImageUrl = "https://blog.jetbrains.com/wp-content/uploads/2023/11/DSGN-17931-Banners-for-1.9.20-release-and-KMP-Stable-annoucement_Blog-Social-share-image-1280x720-1-1.png",
    ),
    FeedItem.TalkingKotlin(
        id = "tag:soundcloud,2010:tracks/1589216519",
        title = "Compose Multiplatform in Production on iOS at Instabee",
        publishTime = "2023-08-09T22:00:00Z".toInstant(),
        contentUrl = "https://soundcloud.com/user-38099918/compose-multiplatform-in-production-at-instabee",
        savedForLater = false,
        audioUrl = "https://feeds.soundcloud.com/stream/1589216519-user-38099918-compose-multiplatform-in-production-at-instabee.mp3",
        thumbnailUrl = "https://i1.sndcdn.com/avatars-000289370353-di6ese-original.jpg",
        summary = "In this episode, we are talking to engineers from @instaboxglobal who use Compose Multiplatform in Production.",
        duration = "55min.",
    ),
).toHomeFeedItems()
