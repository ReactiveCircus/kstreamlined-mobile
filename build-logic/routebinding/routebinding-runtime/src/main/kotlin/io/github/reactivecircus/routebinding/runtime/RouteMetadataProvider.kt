package io.github.reactivecircus.routebinding.runtime

/**
 * Provides Nav3 metadata for a route binding entry.
 *
 * Implementations supply a `Map<String, Any>` that is passed as the `metadata` argument
 * to Nav3's `entry` function in the generated [NavEntryInstaller].
 */
public interface RouteMetadataProvider {
    public fun provide(): Map<String, Any>
}

/**
 * Default no-op [RouteMetadataProvider] that won't be consumed in the generated [NavEntryInstaller].
 */
internal object EmptyMetadataProvider : RouteMetadataProvider {
    override fun provide(): Map<String, Any> = emptyMap()
}
