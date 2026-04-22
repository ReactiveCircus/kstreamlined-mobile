package io.github.reactivecircus.kstreamlined.kmp.pulse.metro

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * CompositionLocal for providing the [PresenterFactory] in Compose.
 * This allows [retainPresenter] and [assistedRetainPresenter] to access a factory.
 */
public val LocalPresenterFactory: ProvidableCompositionLocal<PresenterFactory> =
    staticCompositionLocalOf {
        error("No PresenterFactory registered")
    }
