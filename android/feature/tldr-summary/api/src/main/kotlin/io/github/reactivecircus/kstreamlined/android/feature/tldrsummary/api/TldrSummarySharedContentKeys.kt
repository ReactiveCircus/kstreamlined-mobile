package io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.api

public object TldrSummarySharedContentKeys {
    public fun bounds(origin: String, id: String): String = "Bounds/$origin/$id"

    public fun feedTldrButtonBounds(origin: String, id: String): String = "Bounds/$origin/$id/FeedTldrButton"

    public fun viewerTldrButtonBounds(origin: String, id: String): String = "Bounds/$origin/$id/ViewerTldrButton"

    public fun titleBounds(origin: String, id: String): String = "Bounds/$origin/$id/Title"
}
