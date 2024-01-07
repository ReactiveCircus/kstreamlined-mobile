package io.github.reactivecircus.kstreamlined.kmp.networking.model

public data class FeedSource(
    val key: Key,
    val title: String,
    val description: String,
) {
    public enum class Key {
        KotlinBlog,
        KotlinYouTubeChannel,
        TalkingKotlinPodcast,
        KotlinWeekly,
    }
}
