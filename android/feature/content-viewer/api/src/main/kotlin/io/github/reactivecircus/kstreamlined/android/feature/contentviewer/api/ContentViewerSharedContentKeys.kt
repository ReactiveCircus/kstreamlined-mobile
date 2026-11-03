package io.github.reactivecircus.kstreamlined.android.feature.contentviewer.api

public object ContentViewerSharedContentKeys {
    public fun bounds(origin: String, id: String): String = "Bounds/$origin/$id"

    public fun saveButtonBounds(origin: String, id: String): String = "Bounds/$origin/$id/saveButton"
}
