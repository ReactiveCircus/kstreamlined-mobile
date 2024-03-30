@file:Suppress("MaximumLineLength", "MaxLineLength")

package io.github.reactivecircus.kstreamlined.kmp.remote

import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedEntry
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedSource
import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinWeeklyIssueEntry
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
        audioUrl = "https://feeds.soundcloud.com/stream/1620132927-user-38099918-making-multiplatform-better.mp3",
        thumbnailUrl = "https://i1.sndcdn.com/artworks-zzYOFetx0rxBg1eJ-8bJzmw-t3000x3000.jpg",
        summary = "In this episode, we talk to Kevin Galligan and JP Cathcart from Touchlab about the Kotlin Multiplatform ecosystem and the new Kotlin Multiplatform Mobile (KMM) plugin for Android Studio. We discuss the state of Kotlin Multiplatform, the challenges of building cross-platform libraries, and how KMM helps to address these challenges. We also touch on the new KMM plugin for Android Studio and how it helps to streamline the development of cross-platform apps.",
        duration = "45min.",
    ),
    FeedEntry.KotlinWeekly(
        id = "https://mailchi.mp/kotlinweekly/kotlin-weekly-381",
        title = "Kotlin Weekly #381",
        publishTime = "2023-11-19T09:13:00Z".toInstant(),
        contentUrl = "https://mailchi.mp/kotlinweekly/kotlin-weekly-381",
        issueNumber = 381,
    ),
)

public val FakeKotlinWeeklyIssueEntries: List<KotlinWeeklyIssueEntry> = listOf(
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
