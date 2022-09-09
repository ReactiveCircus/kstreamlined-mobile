@file:Suppress("MaximumLineLength", "MaxLineLength")

package io.github.reactivecircus.kstreamlined.kmm.data.feed

import io.github.reactivecircus.kstreamlined.kmm.apollo.FeedEntriesQuery
import io.github.reactivecircus.kstreamlined.kmm.apollo.FeedSourcesQuery
import io.github.reactivecircus.kstreamlined.kmm.apollo.type.FeedSourceKey
import io.github.reactivecircus.kstreamlined.kmm.apollo.type.buildKotlinBlog
import io.github.reactivecircus.kstreamlined.kmm.apollo.type.buildKotlinWeekly
import io.github.reactivecircus.kstreamlined.kmm.apollo.type.buildKotlinYouTube
import io.github.reactivecircus.kstreamlined.kmm.apollo.type.buildTalkingKotlin

val FakeFeedSources = listOf(
    FeedSourcesQuery.FeedSource(
        key = FeedSourceKey.KOTLIN_BLOG,
        title = "Kotlin Blog",
        description = "Latest news from the official Kotlin Blog",
    ),
    FeedSourcesQuery.FeedSource(
        key = FeedSourceKey.KOTLIN_YOUTUBE_CHANNEL,
        title = "Kotlin YouTube",
        description = "The official YouTube channel of the Kotlin programming language",
    ),
    FeedSourcesQuery.FeedSource(
        key = FeedSourceKey.TALKING_KOTLIN_PODCAST,
        title = "Talking Kotlin",
        description = "Technical show discussing everything Kotlin, hosted by Hadi and Sebastian",
    ),
    FeedSourcesQuery.FeedSource(
        key = FeedSourceKey.KOTLIN_WEEKLY,
        title = "Kotlin Weekly",
        description = "Weekly community Kotlin newsletter, hosted by Enrique",
    ),
)

val FakeFeedEntries = FeedEntriesQuery.Data {
    feedEntries = listOf(
        buildKotlinBlog {
            id = "https://blog.jetbrains.com/?post_type=kotlin&p=264203"
            title = "A New Approach to Incremental Compilation in Kotlin"
            publishTimestamp = "1657882573"
            contentUrl = "https://blog.jetbrains.com/kotlin/2022/07/a-new-approach-to-incremental-compilation-in-kotlin/"
            featuredImageUrl = "https://blog.jetbrains.com/wp-content/uploads/2022/07/A-New-Approach-to-Incremental-Compilation-in-Kotlin-EN-2_Twitter-Blog.png"
            description = "In Kotlin 1.7.0, we’ve reworked incremental compilation for project changes in cross-module dependencies. The new approach lifts previous limitations on incremental compilation. It’s now supported when changes are made inside dependent non-Kotlin modules, and it is compatible with the Gradle build cache. Support for compilation avoidance has also been improved. All of these advancements decrease […]"
        },
        buildKotlinYouTube {
            id = "yt:video:ihMhu3hvCCE"
            title = "Kotlin and Java Interoperability in Spring Projects"
            publishTimestamp = "1657114786"
            contentUrl = "https://www.youtube.com/watch?v=ihMhu3hvCCE"
            thumbnailUrl = "https://i2.ytimg.com/vi/ihMhu3hvCCE/hqdefault.jpg"
            description = "We have configured the Kotlin compiler in a Java/Spring project - now what? Let's talk about important details you need to know about calling Java from Kotlin code and vice versa. Links: Adding Kotlin to Spring/Maven project: https://youtu.be/4-qOxvjjF8g Calling Java from Kotlin: https://kotlinlang.org/docs/java-interop.html Calling Kotlin from Java: https://kotlinlang.org/docs/java-to-kotlin-interop.html Kotlin Spring compiler plugin: https://kotlinlang.org/docs/all-open-plugin.html#spring-support Just starting with Kotlin? Learn Kotlin by creating real-world applications with JetBrains Academy Build simple games, a chat bot, a coffee machine simulator, and other interactive projects step by step in a hands-on learning environment. Get started: https://hyperskill.org/join/fromyoutubetoJetSalesStat?redirect=true&next=/tracks/18 #springboot #springframework #kotlin #interoperability"
        },
        buildTalkingKotlin {
            id = "https://talkingkotlin.com/turbocharging-kotlin-arrow-analysis-optics-meta"
            title = "Turbocharging Kotlin: Arrow Analysis, Optics & Meta"
            publishTimestamp = "1656374400"
            contentUrl = "https://talkingkotlin.com/turbocharging-kotlin-arrow-analysis-optics-meta/"
            podcastLogoUrl = "https://talkingkotlin.com/images/kotlin_talking_logo.png"
            tags = listOf(
                "Arrow",
                "Code Quality",
            )
        },
        buildKotlinWeekly {
            id = "21a2c7f9e24fae1631468c5507e4ff7c"
            title = "Kotlin Weekly #312 has just been published!"
            publishTimestamp = "1658675020"
            contentUrl = "https://t.co/7JzvarYb05"
            newsletterLogoUrl = "https://pbs.twimg.com/profile_images/883969154667204608/26qTz9AE_400x400.jpg"
        }
    )
}.feedEntries
