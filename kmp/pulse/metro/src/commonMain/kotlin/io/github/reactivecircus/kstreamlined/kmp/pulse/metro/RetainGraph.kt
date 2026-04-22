package io.github.reactivecircus.kstreamlined.kmp.pulse.metro

import dev.zacsweers.metro.Multibinds
import dev.zacsweers.metro.Provider
import io.github.reactivecircus.kstreamlined.kmp.pulse.runtime.Presenter
import kotlin.reflect.KClass

/**
 * Contains common multibindings for retained presenter types and their assisted factories.
 *
 * Extend this in a dependency graph that only needs to expose retained provider map multibindings.
 * If you want [MetroRetainFactory] access, extend [RetainGraph] instead.
 *
 * ```kotlin
 * @DependencyGraph
 * interface MyGraph : MetroRetainMultibindings
 * ```
 */
public interface MetroRetainMultibindings {
    @Multibinds // TODO add allowEmpty = true back
    public val retainedProviders: Map<KClass<out Presenter<*, *>>, Provider<Presenter<*, *>>>

    @Multibinds(allowEmpty = true)
    public val assistedFactoryProviders:
        Map<KClass<out RetainedAssistedFactory>, Provider<RetainedAssistedFactory>>
}

/**
 * Extension of [MetroRetainMultibindings] that also includes a [MetroRetainFactory] accessor.
 * Extend this in a dependency graph that needs to expose [MetroRetainFactory].
 *
 * ```kotlin
 * @DependencyGraph
 * interface MyGraph : RetainGraph
 * ```
 */
public interface RetainGraph : MetroRetainMultibindings {
    public val metroRetainFactory: MetroRetainFactory
}
