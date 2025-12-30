package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
public data class TalkingKotlinEpisodeRoute(val origin: String, val id: String) : NavKey
