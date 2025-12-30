package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.api

public object TalkingKotlinEpisodeSharedTransitionKeys {
    public fun bounds(origin: String, id: String): String = "Bounds/$origin/$id"

    public fun playerElement(origin: String, id: String): String = "Element/$origin/$id/player"
}
