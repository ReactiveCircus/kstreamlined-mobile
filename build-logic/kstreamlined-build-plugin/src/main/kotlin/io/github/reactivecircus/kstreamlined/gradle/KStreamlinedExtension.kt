package io.github.reactivecircus.kstreamlined.gradle

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

@DslMarker
internal annotation class KStreamlinedExtensionMarker

/**
 * Entry point for all build configurations in KStreamlined.
 */
@KStreamlinedExtensionMarker
public interface KStreamlinedExtension {
    public fun kmpLibrary(action: Action<KmpLibraryExtension>)

    public fun jvmLibrary(action: Action<JvmLibraryExtension> = Action {})
}

internal abstract class KStreamlinedExtensionImpl @Inject constructor(objects: ObjectFactory) : KStreamlinedExtension {
    private val kmpLibraryExtension = objects.newInstance(KmpLibraryExtensionImpl::class.java)
    private val jvmLibraryExtension = objects.newInstance(JvmLibraryExtensionImpl::class.java)

    private var kmpLibraryConfigured: Boolean = false
    private var jvmLibraryConfigured: Boolean = false

    override fun kmpLibrary(action: Action<KmpLibraryExtension>) {
        action.execute(kmpLibraryExtension)
        kmpLibraryExtension.evaluate()
        kmpLibraryConfigured = true
    }

    override fun jvmLibrary(action: Action<JvmLibraryExtension>) {
        action.execute(jvmLibraryExtension)
        jvmLibraryExtension.evaluate()
        jvmLibraryConfigured = true
    }

    internal fun validate() {
        if (!kmpLibraryConfigured && !jvmLibraryConfigured) {
            error(
                """
                |One of the following top-level configuration blocks must be present in `kstreamlined {...}`:
                |- kmpLibrary {...}
                |- jvmLibrary {...}
                """.trimMargin(),
            )
        }
    }
}
