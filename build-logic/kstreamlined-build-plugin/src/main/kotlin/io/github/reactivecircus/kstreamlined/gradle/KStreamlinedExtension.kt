package io.github.reactivecircus.kstreamlined.gradle

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import javax.inject.Inject

/**
 * Entry point for all build configurations in KStreamlined.
 */
@KStreamlinedExtensionMarker
public interface KStreamlinedExtension {
    public fun kmpLibrary(action: Action<KmpLibraryExtension>)

    public fun jvmLibrary(action: Action<JvmLibraryExtension> = Action {})
}

internal abstract class KStreamlinedExtensionImpl @Inject constructor(objects: ObjectFactory) : KStreamlinedExtension {
    private val kmpLibraryExtension by lazy { objects.newInstance(KmpLibraryExtensionImpl::class.java) }
    private val jvmLibraryExtension by lazy { objects.newInstance(JvmLibraryExtensionImpl::class.java) }

    private var configured = false

    override fun kmpLibrary(action: Action<KmpLibraryExtension>) {
        configureOnce {
            action.execute(kmpLibraryExtension)
            kmpLibraryExtension.evaluate()
        }
    }

    override fun jvmLibrary(action: Action<JvmLibraryExtension>) {
        configureOnce {
            action.execute(jvmLibraryExtension)
            jvmLibraryExtension.evaluate()
        }
    }

    private inline fun configureOnce(block: () -> Unit) {
        require(!configured) {
            """
            |Multiple top-level configuration blocks found in kstreamlined {...}. Only one of the following may be configured:
            |$availableTopLevelExtensions
            """.trimMargin()
        }
        block()
        configured = true
    }

    internal fun validate() {
        require(configured) {
            """
            |Missing top-level configuration block in kstreamlined {...}. One of the following must be configured:
            |$availableTopLevelExtensions
            """.trimMargin()
        }
    }

    private val availableTopLevelExtensions = TopLevelExtension.Kind.entries
        .joinToString(separator = "\n") { "- ${it.extensionName} {...}" }
}

internal interface TopLevelExtension {
    fun evaluate()

    enum class Kind(val extensionName: String) {
        KmpLibrary("kmpLibrary"),
        JvmLibrary("jvmLibrary"),
    }
}

@DslMarker
internal annotation class KStreamlinedExtensionMarker
