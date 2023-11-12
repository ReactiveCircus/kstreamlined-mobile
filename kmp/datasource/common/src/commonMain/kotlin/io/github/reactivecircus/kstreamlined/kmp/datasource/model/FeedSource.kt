package io.github.reactivecircus.kstreamlined.kmp.datasource.model

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
