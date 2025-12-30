package io.github.reactivecircus.kstreamlined.android

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object MainRoute : NavKey

enum class MainPagerItem {
    Home,
    Saved,
}
