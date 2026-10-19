package io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.api

public object TldrSummarySharedTransitionKeys {
    public fun bounds(origin: String, id: String): String = "Bounds/$origin/$id"

    public fun tldrButtonBounds(origin: String, id: String): String = "Bounds/$origin/$id/tldrButton"

    public fun titleBounds(origin: String, id: String): String = "Bounds/$origin/$id/title"
}
