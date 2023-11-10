package io.github.reactivecircus.kstreamlined.kmm.data.feed.mapper

import io.github.reactivecircus.kstreamlined.graphql.FeedEntriesQuery
import io.github.reactivecircus.kstreamlined.kmm.data.feed.model.FeedEntry

internal fun FeedEntriesQuery.FeedEntry.toModel(): FeedEntry {
    return when (this) {
        is FeedEntriesQuery.KotlinBlogFeedEntry -> this.toModel()
        is FeedEntriesQuery.KotlinYouTubeFeedEntry -> this.toModel()
        is FeedEntriesQuery.TalkingKotlinFeedEntry -> this.toModel()
        is FeedEntriesQuery.KotlinWeeklyFeedEntry -> this.toModel()
        else -> error("Unknown FeedEntry subtype")
    }
}

internal fun FeedEntriesQuery.KotlinBlogFeedEntry.toModel(): FeedEntry.KotlinBlog {
    return FeedEntry.KotlinBlog(
        id = this.id,
        title = this.title,
        publishTimestamp = this.publishTimestamp,
        contentUrl = this.contentUrl,
        featuredImageUrl = this.featuredImageUrl,
        description = this.description,
    )
}

internal fun FeedEntriesQuery.KotlinYouTubeFeedEntry.toModel(): FeedEntry.KotlinYouTube {
    return FeedEntry.KotlinYouTube(
        id = this.id,
        title = this.title,
        publishTimestamp = this.publishTimestamp,
        contentUrl = this.contentUrl,
        thumbnailUrl = this.thumbnailUrl,
        description = this.description,
    )
}

internal fun FeedEntriesQuery.TalkingKotlinFeedEntry.toModel(): FeedEntry.TalkingKotlin {
    return FeedEntry.TalkingKotlin(
        id = this.id,
        title = this.title,
        publishTimestamp = this.publishTimestamp,
        contentUrl = this.contentUrl,
        podcastLogoUrl = this.podcastLogoUrl,
        tags = this.tags,
    )
}

internal fun FeedEntriesQuery.KotlinWeeklyFeedEntry.toModel(): FeedEntry.KotlinWeekly {
    return FeedEntry.KotlinWeekly(
        id = this.id,
        title = this.title,
        publishTimestamp = this.publishTimestamp,
        contentUrl = this.contentUrl,
        newsletterLogoUrl = this.newsletterLogoUrl,
    )
}
