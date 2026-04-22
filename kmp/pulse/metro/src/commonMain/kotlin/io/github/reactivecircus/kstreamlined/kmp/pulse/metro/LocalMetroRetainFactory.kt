package io.github.reactivecircus.kstreamlined.kmp.pulse.metro

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * CompositionLocal for providing the [MetroRetainFactory] in Compose.
 * This allows [metroRetain] and [assistedMetroRetain] to access a factory.
 */
public val LocalMetroRetainFactory: ProvidableCompositionLocal<MetroRetainFactory> =
    staticCompositionLocalOf {
        error("No MetroRetainFactory registered")
    }
