package io.github.reactivecircus.kstreamlined.android.feature.settings.api

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
public data class SettingsRoute(val origin: String) : NavKey
