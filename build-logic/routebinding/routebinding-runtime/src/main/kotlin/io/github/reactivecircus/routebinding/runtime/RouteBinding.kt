package io.github.reactivecircus.routebinding.runtime

import androidx.navigation3.runtime.NavKey
import kotlin.reflect.KClass

/**
 * Marks a `@Composable` function as a screen-level navigation entry for the given [route].
 *
 * The compiler plugin generates a [NavEntryInstaller] class that binds the annotated
 * function to a [route] using Nav3's `EntryProviderScope` API, contributing it as a multibinding via Metro.
 *
 * @param route the [NavKey] this Composable function binds to.
 * @param metadataProvider reference to an object that implements [RouteMetadataProvider] to provide Nav3 metadata for this navigation entry.
 *   Defaults to [EmptyMetadataProvider] which won't be consumed in the generated [NavEntryInstaller].
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
public annotation class RouteBinding(
    val route: KClass<out NavKey>,
    val metadataProvider: KClass<out RouteMetadataProvider> = EmptyMetadataProvider::class,
)
