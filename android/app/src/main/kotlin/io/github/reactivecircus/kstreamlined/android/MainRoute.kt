package io.github.reactivecircus.kstreamlined.android

import kotlinx.serialization.Serializable

@Serializable
data object MainRoute : NavRoute

enum class MainPagerItem {
    Home,
    Saved,
}
