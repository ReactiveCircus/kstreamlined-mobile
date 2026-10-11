package io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
public data class TldrSummaryRoute(
    val origin: String,
    val id: String,
    val title: String,
) : NavKey
