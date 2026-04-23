package io.github.reactivecircus.kstreamlined.kmp.capsule.inject

import androidx.compose.runtime.Composable
import androidx.compose.runtime.retain.retain
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.Presenter
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.PresenterAssistedFactory

/**
 * Retains a Metro-injected [Presenter] instance using the [LocalPresenterFactory].
 *
 * The instance is created via the factory on first composition and retained across
 * configuration changes. Since [Presenter] implements `RetainObserver`,
 * lifecycle callbacks are fired automatically.
 */
@Composable
public inline fun <reified T : Presenter<*, *>> retainPresenter(): T {
    val factory = LocalPresenterFactory.current
    return retain { factory.create(T::class) }
}

/**
 * Retains a Metro-injected [Presenter] instance created via a [PresenterAssistedFactory].
 *
 * The factory is resolved from [LocalPresenterFactory], and [block] is invoked
 * on the factory to create the instance.
 */
@Composable
public inline fun <reified T : Presenter<*, *>, reified F : PresenterAssistedFactory> assistedRetainPresenter(
    crossinline block: F.() -> T,
): T {
    val factory = LocalPresenterFactory.current
    return retain { factory.createAssistedFactory(F::class)().block() }
}
