package io.github.reactivecircus.kstreamlined.kmp.pulse.metro

import androidx.compose.runtime.Composable
import androidx.compose.runtime.retain.retain
import io.github.reactivecircus.kstreamlined.kmp.pulse.runtime.Presenter

/**
 * Retains a Metro-injected [Presenter] instance using the [LocalMetroRetainFactory].
 *
 * The instance is created via the factory on first composition and retained across
 * configuration changes. Since [Presenter] implements `RetainObserver`,
 * lifecycle callbacks are fired automatically.
 */
@Composable
public inline fun <reified T : Presenter<*, *>> metroRetain(): T {
    val factory = LocalMetroRetainFactory.current
    return retain { factory.create(T::class) }
}

/**
 * Retains a Metro-injected [Presenter] instance created via a [RetainedAssistedFactory].
 *
 * The factory is resolved from [LocalMetroRetainFactory], and [block] is invoked
 * on the factory to create the instance.
 */
@Composable
public inline fun <reified T : Presenter<*, *>, reified F : RetainedAssistedFactory> assistedMetroRetain(
    crossinline block: F.() -> T,
): T {
    val factory = LocalMetroRetainFactory.current
    return retain { factory.createAssistedFactory(F::class)().block() }
}
