package io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

/**
 * Replaces [current] with [destination], or pops [current] if [destination]
 * is already directly beneath it (single top).
 */
internal fun <T : NavKey> NavBackStack<T>.replaceSingleTop(current: T, destination: T) {
    val index = lastIndexOf(current)
    if (index < 0) return
    if (getOrNull(index - 1) == destination) {
        removeAt(index)
    } else {
        set(index, destination)
    }
}
