package io.github.reactivecircus.kstreamlined.kmp.feed.datasource

import io.github.reactivecircus.kstreamlined.kmp.database.KStreamlinedDatabase
import io.github.reactivecircus.kstreamlined.kmp.feed.datasource.mapper.asExternalModel
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedOrigin
import io.github.reactivecircus.kstreamlined.kmp.model.feed.KotlinWeeklyIssueItem
import io.github.reactivecircus.kstreamlined.kmp.networking.FeedService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.toInstant

public class FeedDataSource(
    private val feedService: FeedService,
    private val database: KStreamlinedDatabase,
) {
    public suspend fun selectFeedSource(feedOrigin: FeedOrigin) {
        feedOrigin.selected
        TODO()
    }

    public suspend fun unselectFeedSource(feedOrigin: FeedOrigin) {
        feedOrigin.selected
        TODO()
    }

    @Suppress("MaxLineLength")
    @OptIn(ExperimentalCoroutinesApi::class)
    public fun streamFeedItemById(id: String): Flow<FeedItem?> {
        database.feedItemEntityQueries
        return savedItemsIds.mapLatest { savedItemsIds ->
            when {
                id.contains("kotlin-weekly") -> {
                    FeedItem.KotlinWeekly(
                        id = id,
                        title = "Kotlin Weekly #386",
                        publishTime = "2023-11-05T08:13:58Z".toInstant(),
                        contentUrl = id,
                        savedForLater = savedItemsIds.contains(id),
                        issueNumber = 386,
                    )
                }

                id.contains("tag:soundcloud") -> fakeTalkingKotlinEpisode.copy(
                    id = id,
                    savedForLater = savedItemsIds.contains(id)
                )

                id.contains("blog.jetbrains.com") -> FeedItem.KotlinBlog(
                    id = id,
                    title = "Tackle Advent of Code 2023 With Kotlin and Win Prizes!",
                    publishTime = "2023-11-23T17:00:38Z".toInstant(),
                    contentUrl = "https://blog.jetbrains.com/kotlin/2023/11/advent-of-code-2023-with-kotlin/",
                    savedForLater = savedItemsIds.contains(id),
                    featuredImageUrl = "https://blog.jetbrains.com/wp-content/uploads/2023/11/DSGN-18072-Social-media-banners_Blog-Featured-image-1280x720-1.png",
                )

                id.contains("yt:video") -> FeedItem.KotlinYouTube(
                    id = id,
                    title = "Coil Goes Multiplatform with Colin White | Talking Kotlin #127",
                    publishTime = "2023-11-29T17:30:08Z".toInstant(),
                    contentUrl = "https://www.youtube.com/watch?v=apiVJfLvUBE",
                    savedForLater = savedItemsIds.contains(id),
                    thumbnailUrl = "https://i2.ytimg.com/vi/apiVJfLvUBE/hqdefault.jpg",
                    description = "Welcome to another engaging episode of Talking Kotlin! In this edition, we dive into the dynamic world of Android development with Colin White, the creator of the widely acclaimed Coil library. Join us as we discuss the latest developments, insights, and the exciting roadmap for Coil. \uD83D\uDE80 Highlights from this Episode: Learn about Colin's journey in developing the Coil library. Discover the pivotal role Coil plays in simplifying image loading for Android developers. Get an exclusive sneak peek into the upcoming Coil 3.0, featuring multi-platform support and seamless integration with Jetpack Compose. \uD83D\uDD17 Helpful Links: Coil Library GitHub: https://coil-kt.github.io/coil/ Follow Colin White on Twitter: https://twitter.com/colinwhi \uD83C\uDF10 Connect with the Kotlin Community: https://kotlinlang.org/community/ Kotlin Foundation: https://kotlinfoundation.org/ \uD83D\uDC49 Don't miss out on the latest insights and updates from the Kotlin world! Subscribe, hit the bell icon, and join the conversation in the comments below. \uD83D\uDCC8 Help us reach 20,000 views by liking, sharing, and subscribing! Your support keeps the Kotlin conversation alive.",
                )

                else -> null
            }
        }.distinctUntilChanged()
    }

    public suspend fun addSavedFeedItem(id: String) {
        savedItemsIds.value += id
    }

    public suspend fun removeSavedFeedItem(id: String) {
        savedItemsIds.value -= id
    }

    public suspend fun loadSavedFeedItems(): List<FeedItem> {
        TODO()
    }

    public suspend fun loadKotlinWeeklyIssue(url: String): List<KotlinWeeklyIssueItem> {
        // TODO persist fetched entry to DB
        return feedService.fetchKotlinWeeklyIssue(url).map {
            it.asExternalModel()
        }
    }

    public suspend fun saveTalkingKotlinEpisodeStartPosition(id: String, positionMillis: Long) {
        // TODO persist to DB
        fakeTalkingKotlinEpisode = fakeTalkingKotlinEpisode.copy(
            id = id,
            startPositionMillis = positionMillis,
        )
    }

    private val savedItemsIds = MutableStateFlow(emptySet<String>())

    @Suppress("MaxLineLength")
    private var fakeTalkingKotlinEpisode = FeedItem.TalkingKotlin(
        id = "tag:soundcloud,2010:tracks/1689535512",
        title = "Coil Goes Multiplatform with Colin White",
        publishTime = "2023-11-29T22:00:00Z".toInstant(),
        contentUrl = "https://soundcloud.com/user-38099918/coil-goes-multiplatform-with-colin-white",
        savedForLater = false,
        audioUrl = "https://feeds.soundcloud.com/stream/1689535512-user-38099918-network-resilient-applications-with-store5-talking-kotlin-128.mp3",
        thumbnailUrl = "https://talkingkotlin.com/images/kotlin_talking_logo.png",
        summary = "Welcome to another engaging episode of Talking Kotlin! In this edition, we dive into the dynamic world of Android development with Colin White, the creator of the widely acclaimed Coil library. Join us as we discuss the latest developments, insights, and the exciting roadmap for Coil. \uD83D\uDE80 Highlights from this Episode: Learn about Colin's journey in developing the Coil library. Discover the pivotal role Coil plays in simplifying image loading for Android developers. Get an exclusive sneak peek into the upcoming Coil 3.0, featuring multi-platform support and seamless integration with Jetpack Compose. \uD83D\uDD17 Helpful Links: Coil Library GitHub: github.com/coilkt/coil Follow Colin White on Twitter: @colinwhi \uD83C\uDF10 Connect with the Kotlin Community: https://kotlinlang.org/community/ Kotlin Foundation: https://kotlinfoundation.org/",
        duration = "42min.",
        startPositionMillis = 0,
    )
}
