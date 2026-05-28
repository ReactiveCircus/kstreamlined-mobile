package io.github.reactivecircus.kstreamlined.android.feature.feedselection.impl

import io.github.reactivecircus.kstreamlined.kmp.feed.model.FeedOrigin

internal object SampleFeedOrigin {
    val KotlinBlog = FeedOrigin(
        key = FeedOrigin.Key.KotlinBlog,
        title = "Kotlin Blog",
        description = "Latest news from the official Kotlin Blog",
        selected = true,
    )

    val KotlinYouTubeChannel = FeedOrigin(
        key = FeedOrigin.Key.KotlinYouTubeChannel,
        title = "Kotlin YouTube Channel",
        description = "Videos from the official Kotlin YouTube channel",
        selected = true,
    )

    val TalkingKotlinPodcast = FeedOrigin(
        key = FeedOrigin.Key.TalkingKotlinPodcast,
        title = "Talking Kotlin Podcast",
        description = "Podcast on Kotlin and more by JetBrains",
        selected = true,
    )

    val KotlinWeekly = FeedOrigin(
        key = FeedOrigin.Key.KotlinWeekly,
        title = "Kotlin Weekly",
        description = "Weekly community Kotlin newsletter",
        selected = true,
    )
}
