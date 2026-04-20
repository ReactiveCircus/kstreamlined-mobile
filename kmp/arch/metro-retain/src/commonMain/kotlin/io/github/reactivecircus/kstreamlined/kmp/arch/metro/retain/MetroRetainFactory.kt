package io.github.reactivecircus.kstreamlined.kmp.arch.metro.retain

import dev.zacsweers.metro.Provider
import kotlin.reflect.KClass

/**
 * Factory class for creating retained instances and their assisted factories.
 *
 * This can be provided on a DI graph and then installed via [LocalMetroRetainFactory].
 * It can be used in tandem with [RetainGraph].
 *
 * Subclass this class and contribute an injected binding. Override whichever provider maps
 * you need to support.
 *
 * ```kotlin
 * @DependencyGraph(AppScope::class)
 * interface MyGraph : RetainGraph
 *
 * @Inject
 * @ContributesBinding(AppScope::class)
 * @SingleIn(AppScope::class)
 * class MyRetainFactory(
 *   override val retainedProviders: Map<KClass<out Retainable>, Provider<Retainable>>,
 *   override val assistedFactoryProviders: Map<KClass<out RetainedAssistedFactory>, Provider<RetainedAssistedFactory>>,
 * ) : MetroRetainFactory()
 * ```
 */
@Suppress("MaxLineLength")
public abstract class MetroRetainFactory {
    protected open val retainedProviders: Map<KClass<out Retainable>, Provider<Retainable>> = emptyMap()
    protected open val assistedFactoryProviders:
        Map<KClass<out RetainedAssistedFactory>, Provider<RetainedAssistedFactory>> =
        emptyMap()

    @Suppress("UNCHECKED_CAST")
    public fun <T : Retainable> create(modelClass: KClass<T>): T {
        retainedProviders[modelClass]?.let { provider ->
            return provider() as T
        }
        throw IllegalArgumentException("No retained provider found for $modelClass")
    }

    public fun <FactoryType : RetainedAssistedFactory> createAssistedFactory(
        factoryClass: KClass<FactoryType>,
    ): Provider<FactoryType> {
        assistedFactoryProviders[factoryClass]?.let { provider ->
            @Suppress("UNCHECKED_CAST")
            return provider as Provider<FactoryType>
        }
        error("No retained assisted factory provider found for $factoryClass")
    }
}
