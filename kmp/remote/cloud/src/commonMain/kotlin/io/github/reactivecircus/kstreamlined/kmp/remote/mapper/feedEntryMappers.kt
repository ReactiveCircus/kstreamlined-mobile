package io.github.reactivecircus.kstreamlined.kmp.remote.mapper

import io.github.reactivecircus.kstreamlined.graphql.fragment.FeedEntryItem
import io.github.reactivecircus.kstreamlined.graphql.fragment.KotlinBlogFeedEntryItem
import io.github.reactivecircus.kstreamlined.graphql.fragment.KotlinWeeklyFeedEntryItem
import io.github.reactivecircus.kstreamlined.graphql.fragment.KotlinYouTubeFeedEntryItem
import io.github.reactivecircus.kstreamlined.graphql.fragment.TalkingKotlinFeedEntryItem
import io.github.reactivecircus.kstreamlined.kmp.remote.model.FeedEntry

internal fun FeedEntryItem.asExternalModel(): FeedEntry {
    return when (this) {
        is KotlinBlogFeedEntryItem -> this.asExternalModel()
        is KotlinYouTubeFeedEntryItem -> this.asExternalModel()
        is TalkingKotlinFeedEntryItem -> this.asExternalModel()
        is KotlinWeeklyFeedEntryItem -> this.asExternalModel()
        else -> error("Unknown FeedEntry subtype")
    }
}

internal fun KotlinBlogFeedEntryItem.asExternalModel(): FeedEntry.KotlinBlog {
    return FeedEntry.KotlinBlog(
        id = this.id,
        title = this.title,
        publishTime = this.publishTime,
        contentUrl = this.contentUrl,
        featuredImageUrl = this.onKotlinBlog.featuredImageUrl,
    )
}

internal fun KotlinYouTubeFeedEntryItem.asExternalModel(): FeedEntry.KotlinYouTube {
    return FeedEntry.KotlinYouTube(
        id = this.id,
        title = this.title,
        publishTime = this.publishTime,
        contentUrl = this.contentUrl,
        thumbnailUrl = this.onKotlinYouTube.thumbnailUrl,
        description = this.onKotlinYouTube.description,
    )
}

internal fun TalkingKotlinFeedEntryItem.asExternalModel(): FeedEntry.TalkingKotlin {
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

internal fun KotlinWeeklyFeedEntryItem.asExternalModel(): FeedEntry.KotlinWeekly {
    return FeedEntry.KotlinWeekly(
        id = this.id,
        title = this.title,
        publishTime = this.publishTime,
        contentUrl = this.contentUrl,
        issueNumber = this.onKotlinWeekly.issueNumber,
    )
}
