package io.github.reactivecircus.kstreamlined.android.feature.kotlinyoutubevideo.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
public data class KotlinYouTubeVideoRoute(val origin: String, val id: String) : NavKey
