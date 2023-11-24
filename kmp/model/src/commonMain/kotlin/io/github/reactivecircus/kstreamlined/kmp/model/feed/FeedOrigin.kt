package io.github.reactivecircus.kstreamlined.kmp.model.feed

public data class FeedOrigin(
    val key: Key,
    val title: String,
    val description: String,
    val selected: Boolean,
) {
    public enum class Key {
        KotlinBlog,
        KotlinYouTubeChannel,
        TalkingKotlinPodcast,
        KotlinWeekly,
    }
}
