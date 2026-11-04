package io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.api

public object TldrSummarySharedContentKeys {
    public fun bounds(origin: String, id: String): String = "Bounds/$origin/$id"

    public fun feedTldrButtonBounds(origin: String, id: String): String = "Bounds/$origin/$id/feedTldrButton"

    public fun viewerTldrButtonBounds(origin: String, id: String): String = "Bounds/$origin/$id/viewerTldrButton"

    public fun titleBounds(origin: String, id: String): String = "Bounds/$origin/$id/title"
}
