package io.github.reactivecircus.kstreamlined.kmp.feed.datasource.model

data class FeedOrigin(
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
