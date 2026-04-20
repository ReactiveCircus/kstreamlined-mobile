package io.github.reactivecircus.kstreamlined.kmp.arch.metro.retain

import androidx.compose.runtime.Composable
import androidx.compose.runtime.retain.retain

/**
 * Retains a Metro-injected instance using the [LocalMetroRetainFactory].
 *
 * The instance is created via the factory on first composition and retained across
 * configuration changes. If the created instance implements `RetainObserver`,
 * lifecycle callbacks are fired automatically.
 */
@Composable
public inline fun <reified T : Retainable> metroRetain(): T {
    val factory = LocalMetroRetainFactory.current
    return retain { factory.create(T::class) }
}

/**
 * Retains a Metro-injected instance created via a [RetainedAssistedFactory].
 *
 * The factory is resolved from [LocalMetroRetainFactory], and [block] is invoked
 * on the factory to create the instance.
 */
@Composable
public inline fun <reified T : Retainable, reified F : RetainedAssistedFactory> assistedMetroRetain(
    crossinline block: F.() -> T,
): T {
    val factory = LocalMetroRetainFactory.current
    return retain { factory.createAssistedFactory(F::class)().block() }
}
