@file:Suppress("MaxLineLength")

package io.github.reactivecircus.kstreamlined.kmp.remote

import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedSource
import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinWeeklyIssueEntry
import kotlin.time.Instant

internal val MockFeedSources: List<FeedSource> = listOf(
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

internal val MockFeedEntries: List<FeedEntry> = listOf(
    FeedEntry.KotlinYouTube(
        id = "yt:video:apiVJfLvUBE",
        title = "Coil Goes Multiplatform with Colin White | Talking Kotlin #127",
        publishTime = Instant.parse("2023-11-29T17:30:08Z"),
        contentUrl = "https://www.youtube.com/watch?v=apiVJfLvUBE",
        thumbnailUrl = "https://i2.ytimg.com/vi/apiVJfLvUBE/hqdefault.jpg",
        description = "Welcome to another engaging episode of Talking Kotlin! In this edition, we dive into the dynamic world of Android development with Colin White, the creator of the widely acclaimed Coil library. Join us as we discuss the latest developments, insights, and the exciting roadmap for Coil. \uD83D\uDE80 Highlights from this Episode: Learn about Colin's journey in developing the Coil library. Discover the pivotal role Coil plays in simplifying image loading for Android developers. Get an exclusive sneak peek into the upcoming Coil 3.0, featuring multi-platform support and seamless integration with Jetpack Compose. \uD83D\uDD17 Helpful Links: Coil Library GitHub: https://coil-kt.github.io/coil/ Follow Colin White on Twitter: https://twitter.com/colinwhi \uD83C\uDF10 Connect with the Kotlin Community: https://kotlinlang.org/community/ Kotlin Foundation: https://kotlinfoundation.org/ \uD83D\uDC49 Don't miss out on the latest insights and updates from the Kotlin world! Subscribe, hit the bell icon, and join the conversation in the comments below. \uD83D\uDCC8 Help us reach 20,000 views by liking, sharing, and subscribing! Your support keeps the Kotlin conversation alive.",
    ),
    FeedEntry.KotlinWeekly(
        id = "https://mailchi.mp/kotlinweekly/kotlin-weekly-382",
        title = "Kotlin Weekly #382",
        publishTime = Instant.parse("2023-11-26T11:14:09Z"),
        contentUrl = "https://mailchi.mp/kotlinweekly/kotlin-weekly-382",
        issueNumber = 382,
    ),
    FeedEntry.KotlinBlog(
        id = "https://blog.jetbrains.com/?post_type=kotlin&p=409489",
        title = "Tackle Advent of Code 2023 With Kotlin and Win Prizes!",
        publishTime = Instant.parse("2023-11-23T17:00:38Z"),
        contentUrl = "https://blog.jetbrains.com/kotlin/2023/11/advent-of-code-2023-with-kotlin/",
        featuredImageUrl = "https://blog.jetbrains.com/wp-content/uploads/2023/11/DSGN-18072-Social-media-banners_Blog-Featured-image-1280x720-1.png",
    ),
    FeedEntry.KotlinYouTube(
        id = "yt:video:bz4cQeaXmsI",
        title = "The State of Kotlin Multiplatform",
        publishTime = Instant.parse("2023-11-21T18:47:47Z"),
        contentUrl = "https://www.youtube.com/watch?v=bz4cQeaXmsI",
        thumbnailUrl = "https://i3.ytimg.com/vi/bz4cQeaXmsI/hqdefault.jpg",
        description = "JetBrains Kotlin Multiplatform (KMP) is an open-source technology designed for flexible cross-platform development. It allows you to develop apps for Android, iOS, desktop, web, and server-side and efficiently reuse code across them, all while retaining the benefits of native programming. After 8 years of development, KMP has been refined into a production-ready technology and is going Stable, which means now is a great time to start using it in your project.",
    ),
    FeedEntry.KotlinWeekly(
        id = "https://mailchi.mp/kotlinweekly/kotlin-weekly-381",
        title = "Kotlin Weekly #381",
        publishTime = Instant.parse("2023-11-19T09:13:00Z"),
        contentUrl = "https://mailchi.mp/kotlinweekly/kotlin-weekly-381",
        issueNumber = 381,
    ),
    FeedEntry.KotlinBlog(
        id = "https://blog.jetbrains.com/?post_type=kotlin&p=405553",
        title = "Kotlin Multiplatform Development Roadmap for 2024",
        publishTime = Instant.parse("2023-11-16T11:59:46Z"),
        contentUrl = "https://blog.jetbrains.com/kotlin/2023/11/kotlin-multiplatform-development-roadmap-for-2024/",
        featuredImageUrl = "https://blog.jetbrains.com/wp-content/uploads/2023/11/kmp_roadmap_720.png",
    ),
    FeedEntry.TalkingKotlin(
        id = "tag:soundcloud,2010:tracks/1664789634",
        title = "http4k Chronicles",
        publishTime = Instant.parse("2023-11-13T23:00:00Z"),
        contentUrl = "https://soundcloud.com/user-38099918/http4k-chronicles",
        audioUrl = "https://feeds.soundcloud.com/stream/1664789634-user-38099918-http4k-chronicles.mp3",
        thumbnailUrl = "https://i1.sndcdn.com/artworks-uP9Cxy5KSYNzGebf-3q1MsQ-t3000x3000.jpg",
        summary = "Dive into the intricate world of microservices with the latest episode of Talking Kotlin, hosted by Sebastian and Hadi.",
        duration = "56min.",
    ),
    FeedEntry.KotlinBlog(
        id = "https://blog.jetbrains.com/?post_type=blog&p=404245",
        title = "Amper – Improving the Build Tooling User Experience",
        publishTime = Instant.parse("2023-11-09T10:07:24Z"),
        contentUrl = "https://blog.jetbrains.com/blog/2023/11/09/amper-improving-the-build-tooling-user-experience/",
        featuredImageUrl = "https://blog.jetbrains.com/wp-content/uploads/2023/11/Blog-Featured-1280x720-2x-1-2.png",
    ),
    FeedEntry.KotlinBlog(
        id = "https://blog.jetbrains.com/?post_type=kotlin&p=403389",
        title = "Welcome Fleet with Kotlin Multiplatform Tooling",
        publishTime = Instant.parse("2023-11-07T14:24:34Z"),
        contentUrl = "https://blog.jetbrains.com/kotlin/2023/11/kotlin-multiplatform-tooling-in-fleet/",
        featuredImageUrl = "https://blog.jetbrains.com/wp-content/uploads/2023/11/Blog-Featured-1280x720-2x.png",
    ),
    FeedEntry.KotlinWeekly(
        id = "https://mailchi.mp/kotlinweekly/kotlin-weekly-379",
        title = "Kotlin Weekly #379",
        publishTime = Instant.parse("2023-11-05T08:13:58Z"),
        contentUrl = "https://mailchi.mp/kotlinweekly/kotlin-weekly-379",
        issueNumber = 379,
    ),
    FeedEntry.KotlinBlog(
        id = "https://blog.jetbrains.com/?post_type=kotlin&p=401216",
        title = "Compose Multiplatform 1.5.10 – The Perfect Time To Get Started",
        publishTime = Instant.parse("2023-11-02T12:01:27Z"),
        contentUrl = "https://blog.jetbrains.com/kotlin/2023/11/compose-multiplatform-1-5-10-release/",
        featuredImageUrl = "https://blog.jetbrains.com/wp-content/uploads/2023/11/compose-featured_blog_1280x720.png",
    ),
    FeedEntry.KotlinYouTube(
        id = "yt:video:Ol_96CHKqg8",
        title = "What's new in Kotlin 1.9.20",
        publishTime = Instant.parse("2023-11-01T12:44:00Z"),
        contentUrl = "https://www.youtube.com/watch?v=Ol_96CHKqg8",
        thumbnailUrl = "https://i4.ytimg.com/vi/Ol_96CHKqg8/hqdefault.jpg",
        description = "The Kotlin 1.9.20 release is out, and the K2 compiler for all the targets is now in Beta.",
    ),
    FeedEntry.KotlinBlog(
        id = "https://blog.jetbrains.com/?post_type=kotlin&p=401121",
        title = "Kotlin Multiplatform Is Stable and Production-Ready",
        publishTime = Instant.parse("2023-11-01T11:31:28Z"),
        contentUrl = "https://blog.jetbrains.com/kotlin/2023/11/kotlin-multiplatform-stable/",
        featuredImageUrl = "https://blog.jetbrains.com/wp-content/uploads/2023/11/DSGN-17931-Banners-for-1.9.20-release-and-KMP-Stable-annoucement_Blog-Social-share-image-1280x720-1-1.png",
    ),
    FeedEntry.TalkingKotlin(
        id = "tag:soundcloud,2010:tracks/1589216519",
        title = "Compose Multiplatform in Production on iOS at Instabee",
        publishTime = Instant.parse("2023-08-09T22:00:00Z"),
        contentUrl = "https://soundcloud.com/user-38099918/compose-multiplatform-in-production-at-instabee",
        audioUrl = "https://feeds.soundcloud.com/stream/1589216519-user-38099918-compose-multiplatform-in-production-at-instabee.mp3",
        thumbnailUrl = "https://i1.sndcdn.com/avatars-000289370353-di6ese-original.jpg",
        summary = "In this episode, we are talking to engineers from @instaboxglobal who use Compose Multiplatform in Production.",
        duration = "55min.",
    ),
)

internal val MockKotlinWeeklyIssueEntries: List<KotlinWeeklyIssueEntry> = listOf(
    KotlinWeeklyIssueEntry(
        title = "Amper Update – December 2023",
        summary = "Last month JetBrains introduced Amper, a tool to improve the project configuration user experience. Marton Braun gives us an update about its state in December 2023.",
        url = "https://blog.jetbrains.com/amper/2023/12/amper-update-december-2023/",
        source = "blog.jetbrains.com",
        group = KotlinWeeklyIssueEntry.Group.Announcements,
    ),
    KotlinWeeklyIssueEntry(
        title = "How to Use the Cucumber Framework to Test Application Use Cases",
        summary = "Matthias Schenk writes today about Cucumber, a framework that can be used in application development to verify the correct behavior of the application.",
        url = "https://towardsdev.com/how-to-use-the-cucumber-framework-to-test-application-use-cases-48b4f21ee0d0",
        source = "towardsdev.com",
        group = KotlinWeeklyIssueEntry.Group.Articles,
    ),
    KotlinWeeklyIssueEntry(
        title = "Using launcher and themed icons in Android Studio, the manual way",
        summary = "Marlon Lòpez describes how we can use the launcher and the themed icons in Android Studio.",
        url = "https://dev.to/marlonlom/using-launcher-and-themed-icons-in-android-studio-the-manual-way-1h2a",
        source = "dev.to",
        group = KotlinWeeklyIssueEntry.Group.Android,
    ),
    KotlinWeeklyIssueEntry(
        title = "Setting Sail with Compose Multiplatform by Isuru Rajapakse",
        summary = "Isuru Rajapakse talks at the DevFest Sri Lanka about Compose Multiplatform.",
        url = "https://www.youtube.com/watch?v=sG60644C47I",
        source = "www.youtube.com",
        group = KotlinWeeklyIssueEntry.Group.Videos,
    ),
    KotlinWeeklyIssueEntry(
        title = "Kim - Kotlin Image Metadata",
        summary = "Kim is a Kotlin image metadata manipulation library for Kotlin Multiplatform.",
        url = "https://github.com/Ashampoo/kim",
        source = "github.com",
        group = KotlinWeeklyIssueEntry.Group.Libraries,
    ),
)
