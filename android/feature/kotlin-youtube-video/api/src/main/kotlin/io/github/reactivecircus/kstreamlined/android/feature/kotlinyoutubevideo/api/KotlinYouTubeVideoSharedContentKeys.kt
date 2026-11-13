package io.github.reactivecircus.kstreamlined.android.feature.kotlinyoutubevideo.api

public object KotlinYouTubeVideoSharedContentKeys {
    public fun bounds(origin: String, id: String): String = "Bounds/$origin/$id"

    public fun thumbnailBounds(origin: String, id: String): String = "Bounds/$origin/$id/Thumbnail"

    public fun saveButtonBounds(origin: String, id: String): String = "Bounds/$origin/$id/SaveButton"
}
