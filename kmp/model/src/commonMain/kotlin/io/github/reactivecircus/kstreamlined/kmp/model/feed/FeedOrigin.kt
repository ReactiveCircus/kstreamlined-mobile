package io.github.reactivecircus.kstreamlined.kmp.model.feed

import androidx.compose.runtime.Immutable

@Immutable
public data class FeedOrigin(
    val key: Key,
    val title: String,
    val description: String,
    val selected: Boolean,
) {
    @Immutable
    public enum class Key {
        KotlinBlog,
        KotlinYouTubeChannel,
        TalkingKotlinPodcast,
        KotlinWeekly,
    }
}
