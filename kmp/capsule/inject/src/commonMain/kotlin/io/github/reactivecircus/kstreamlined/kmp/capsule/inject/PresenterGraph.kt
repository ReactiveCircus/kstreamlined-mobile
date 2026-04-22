package io.github.reactivecircus.kstreamlined.kmp.capsule.inject

import dev.zacsweers.metro.Multibinds
import dev.zacsweers.metro.Provider
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.Presenter
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.PresenterAssistedFactory
import kotlin.reflect.KClass

/**
 * Contains common multibindings for [Presenter] types and their assisted factories.
 *
 * Extend this in a dependency graph that only needs to expose presenter provider map multibindings.
 * If you want [PresenterFactory] access, extend [PresenterGraph] instead.
 *
 * ```kotlin
 * @DependencyGraph
 * interface MyGraph : PresenterMultibindings
 * ```
 */
public interface PresenterMultibindings {
    @Multibinds // TODO add allowEmpty = true back
    public val presenterProviders: Map<KClass<out Presenter<*, *>>, Provider<Presenter<*, *>>>

    @Multibinds(allowEmpty = true)
    public val assistedFactoryProviders:
        Map<KClass<out PresenterAssistedFactory>, Provider<PresenterAssistedFactory>>
}

/**
 * Extension of [PresenterMultibindings] that also includes a [PresenterFactory] accessor.
 * Extend this in a dependency graph that needs to expose [PresenterFactory].
 *
 * ```kotlin
 * @DependencyGraph
 * interface MyGraph : PresenterGraph
 * ```
 */
public interface PresenterGraph : PresenterMultibindings {
    public val presenterFactory: PresenterFactory
}
