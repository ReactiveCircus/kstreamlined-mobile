package io.github.reactivecircus.kstreamlined.kmp.capsule.inject

import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.Presenter
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.PresenterAssistedFactory
import kotlin.reflect.KClass

/**
 * Factory class for creating [Presenter] instances and their assisted factories via Metro DI.
 *
 * Holds provider maps populated by Metro's multibinding mechanism. Subclass this class and
 * contribute an injected binding:
 *
 * ```kotlin
 * @DependencyGraph(AppScope::class)
 * interface MyGraph : PresenterGraph
 *
 * @Inject
 * @ContributesBinding(AppScope::class)
 * @SingleIn(AppScope::class)
 * class MyPresenterFactory(
 *   override val presenterProviders: Map<KClass<out Presenter<*, *>>, () -> Presenter<*, *>>,
 *   override val assistedFactoryProviders: Map<KClass<out PresenterAssistedFactory>, () -> PresenterAssistedFactory>,
 * ) : PresenterFactory()
 * ```
 */
@Suppress("MaxLineLength")
public abstract class PresenterFactory {
    protected open val presenterProviders: Map<KClass<out Presenter<*, *>>, () -> Presenter<*, *>> = emptyMap()
    protected open val assistedFactoryProviders:
        Map<KClass<out PresenterAssistedFactory>, () -> PresenterAssistedFactory> =
        emptyMap()

    @Suppress("UNCHECKED_CAST")
    public fun <T : Presenter<*, *>> create(modelClass: KClass<T>): T {
        presenterProviders[modelClass]?.let { provider ->
            return provider() as T
        }
        throw IllegalArgumentException("No presenter provider found for $modelClass")
    }

    public fun <FactoryType : PresenterAssistedFactory> createAssistedFactory(
        factoryClass: KClass<FactoryType>,
    ): () -> FactoryType {
        assistedFactoryProviders[factoryClass]?.let { provider ->
            @Suppress("UNCHECKED_CAST")
            return provider as () -> FactoryType
        }
        error("No presenter assisted factory provider found for $factoryClass")
    }
}
