package io.github.reactivecircus.kstreamlined.kmm.data.feed.model

data class FeedSource(
    val key: Key,
    val title: String,
    val description: String,
) {
    enum class Key {
        KotlinBlog,
        KotlinYouTubeChannel,
        TalkingKotlinPodcast,
        KotlinWeekly,
    }
}
