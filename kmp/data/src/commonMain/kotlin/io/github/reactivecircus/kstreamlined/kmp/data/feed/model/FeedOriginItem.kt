package io.github.reactivecircus.kstreamlined.kmp.data.feed.model

data class FeedOriginItem(
    val key: Key,
    val title: String,
    val description: String,
    val selected: Boolean,
) {
    enum class Key {
        KotlinBlog,
        KotlinYouTubeChannel,
        TalkingKotlinPodcast,
        KotlinWeekly,
    }
}
