package io.github.reactivecircus.kstreamlined.android.feature.contentviewer.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
public data class ContentViewerRoute(val origin: String, val id: String) : NavKey
