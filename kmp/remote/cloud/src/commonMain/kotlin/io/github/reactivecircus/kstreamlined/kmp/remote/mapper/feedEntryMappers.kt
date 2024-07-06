package io.github.reactivecircus.kstreamlined.kmp.remote.mapper

import io.github.reactivecircus.kstreamlined.graphql.FeedEntriesQuery
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedEntry

internal fun FeedEntriesQuery.FeedEntry.asExternalModel(): FeedEntry {
    return when (this) {
        is FeedEntriesQuery.KotlinBlogFeedEntry -> this.asExternalModel()
        is FeedEntriesQuery.KotlinYouTubeFeedEntry -> this.asExternalModel()
        is FeedEntriesQuery.TalkingKotlinFeedEntry -> this.asExternalModel()
        is FeedEntriesQuery.KotlinWeeklyFeedEntry -> this.asExternalModel()
        else -> error("Unknown FeedEntry subtype")
    }
}

internal fun FeedEntriesQuery.KotlinBlogFeedEntry.asExternalModel(): FeedEntry.KotlinBlog {
    return FeedEntry.KotlinBlog(
        id = this.id,
        title = this.title,
        publishTime = this.publishTime,
        contentUrl = this.contentUrl,
        featuredImageUrl = this.onKotlinBlog.featuredImageUrl,
    )
}

internal fun FeedEntriesQuery.KotlinYouTubeFeedEntry.asExternalModel(): FeedEntry.KotlinYouTube {
    return FeedEntry.KotlinYouTube(
        id = this.id,
        title = this.title,
        publishTime = this.publishTime,
        contentUrl = this.contentUrl,
        thumbnailUrl = this.onKotlinYouTube.thumbnailUrl,
        description = this.onKotlinYouTube.description,
    )
}

internal fun FeedEntriesQuery.TalkingKotlinFeedEntry.asExternalModel(): FeedEntry.TalkingKotlin {
    return FeedEntry.TalkingKotlin(
        id = this.id,
        title = this.title,
        publishTime = this.publishTime,
        contentUrl = this.contentUrl,
        audioUrl = this.onTalkingKotlin.audioUrl,
        thumbnailUrl = this.onTalkingKotlin.thumbnailUrl,
        summary = this.onTalkingKotlin.summary,
        duration = this.onTalkingKotlin.duration,
    )
}

internal fun FeedEntriesQuery.KotlinWeeklyFeedEntry.asExternalModel(): FeedEntry.KotlinWeekly {
    return FeedEntry.KotlinWeekly(
        id = this.id,
        title = this.title,
        publishTime = this.publishTime,
        contentUrl = this.contentUrl,
        issueNumber = this.onKotlinWeekly.issueNumber,
    )
}
