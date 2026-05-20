package io.github.reactivecircus.kstreamlined.android.feature.feedselection.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
public data class FeedSelectionRoute(val origin: String) : NavKey
