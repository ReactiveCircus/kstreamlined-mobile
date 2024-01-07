package io.github.reactivecircus.kstreamlined.kmp.data.feed

import co.touchlab.kermit.Logger
import io.github.reactivecircus.kstreamlined.kmp.data.feed.mapper.asExternalModel
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedOrigin
import io.github.reactivecircus.kstreamlined.kmp.model.feed.KotlinWeeklyIssueItem
import io.github.reactivecircus.kstreamlined.kmp.networking.FeedService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.datetime.toInstant

public class FeedRepository(
    private val feedService: FeedService
) {

    public val feedSyncState: Flow<FeedSyncState> = emptyFlow()

    public suspend fun syncNow() {
        feedService.fetchFeedEntries(null, true).also {
            Logger.i("<<Number of entries: ${it.size}>>")
            it.forEach { entry ->
                Logger.i("${entry::class.simpleName}: ${entry.title}, ${entry.publishTime}}")
            }
        }
    }

    public suspend fun selectFeedSource(feedOrigin: FeedOrigin) {
        feedOrigin.selected
        TODO()
    }

    public suspend fun unselectFeedSource(feedOrigin: FeedOrigin) {
        feedOrigin.selected
        TODO()
    }

    public suspend fun loadFeedItemById(id: String): FeedItem? {
        // TODO load from DB
        return when {
            id.contains("kotlin-weekly") -> {
                FeedItem.KotlinWeekly(
                    id = id,
                    title = "Kotlin Weekly #386",
                    publishTime = "2023-11-05T08:13:58Z".toInstant(),
                    contentUrl = id,
                    savedForLater = false,
                    issueNumber = 386,
                )
            }
            id.contains("tag:soundcloud") -> {
                @Suppress("MaxLineLength")
                FeedItem.TalkingKotlin(
                    id = id,
                    title = "Coil Goes Multiplatform with Colin White",
                    publishTime = "2023-11-29T22:00:00Z".toInstant(),
                    contentUrl = "https://soundcloud.com/user-38099918/coil-goes-multiplatform-with-colin-white",
                    savedForLater = false,
                    audioUrl = "https://feeds.soundcloud.com/stream/1689535512-user-38099918-network-resilient-applications-with-store5-talking-kotlin-128.mp3",
                    thumbnailUrl = "https://talkingkotlin.com/images/kotlin_talking_logo.png",
                    summary = "Welcome to another engaging episode of Talking Kotlin! In this edition, we dive into the dynamic world of Android development with Colin White, the creator of the widely acclaimed Coil library. Join us as we discuss the latest developments, insights, and the exciting roadmap for Coil. \uD83D\uDE80 Highlights from this Episode: Learn about Colin's journey in developing the Coil library. Discover the pivotal role Coil plays in simplifying image loading for Android developers. Get an exclusive sneak peek into the upcoming Coil 3.0, featuring multi-platform support and seamless integration with Jetpack Compose. \uD83D\uDD17 Helpful Links: Coil Library GitHub: github.com/coilkt/coil Follow Colin White on Twitter: @colinwhi \uD83C\uDF10 Connect with the Kotlin Community: https://kotlinlang.org/community/ Kotlin Foundation: https://kotlinfoundation.org/",
                    duration = "42min.",
                )
            }
            else -> null
        }
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
}
