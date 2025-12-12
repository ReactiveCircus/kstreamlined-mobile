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

    public fun androidCoreLibrary(namespace: String, action: Action<AndroidCoreLibraryExtension> = Action {})

    public fun androidFeatureLibrary(namespace: String, action: Action<AndroidFeatureLibraryExtension> = Action {})

    public fun androidApp(
        namespace: String,
        applicationId: String,
        baseApkName: String,
        action: Action<AndroidAppExtension>,
    )

    public fun androidBenchmark(
        namespace: String,
        targetProjectPath: String,
        environment: String,
        targetAppIdKey: String,
        minSdk: Int,
        action: Action<AndroidBenchmarkExtension>,
    )
}

internal abstract class KStreamlinedExtensionImpl @Inject constructor(
    private val objects: ObjectFactory,
) : KStreamlinedExtension {
    private val kmpLibraryExtension by lazy { objects.newInstance(KmpLibraryExtensionImpl::class.java) }
    private val jvmLibraryExtension by lazy { objects.newInstance(JvmLibraryExtensionImpl::class.java) }
    private var androidCoreLibraryExtension: AndroidCoreLibraryExtensionImpl? = null
    private var androidFeatureLibraryExtension: AndroidFeatureLibraryExtensionImpl? = null
    private var androidAppExtension: AndroidAppExtensionImpl? = null
    private var androidBenchmarkExtension: AndroidBenchmarkExtensionImpl? = null

    private var configured = false

    override fun kmpLibrary(action: Action<KmpLibraryExtension>) {
        configureTopLevelDsl {
            action.execute(kmpLibraryExtension)
            kmpLibraryExtension.evaluate()
        }
    }

    override fun jvmLibrary(action: Action<JvmLibraryExtension>) {
        configureTopLevelDsl {
            action.execute(jvmLibraryExtension)
            jvmLibraryExtension.evaluate()
        }
    }

    override fun androidCoreLibrary(namespace: String, action: Action<AndroidCoreLibraryExtension>) {
        configureTopLevelDsl {
            if (androidCoreLibraryExtension == null) {
                androidCoreLibraryExtension = objects.newInstance(
                    AndroidCoreLibraryExtensionImpl::class.java,
                    namespace,
                )
            }
            val extension = androidCoreLibraryExtension!!
            action.execute(extension)
            extension.evaluate()
        }
    }

    override fun androidFeatureLibrary(namespace: String, action: Action<AndroidFeatureLibraryExtension>) {
        configureTopLevelDsl {
            if (androidFeatureLibraryExtension == null) {
                androidFeatureLibraryExtension = objects.newInstance(
                    AndroidFeatureLibraryExtensionImpl::class.java,
                    namespace,
                )
            }
            val extension = androidFeatureLibraryExtension!!
            action.execute(extension)
            extension.evaluate()
        }
    }

    override fun androidApp(
        namespace: String,
        applicationId: String,
        baseApkName: String,
        action: Action<AndroidAppExtension>,
    ) {
        configureTopLevelDsl {
            if (androidAppExtension == null) {
                androidAppExtension = objects.newInstance(
                    AndroidAppExtensionImpl::class.java,
                    namespace,
                    applicationId,
                    baseApkName,
                )
            }
            val extension = androidAppExtension!!
            action.execute(extension)
            extension.evaluate()
        }
    }

    override fun androidBenchmark(
        namespace: String,
        targetProjectPath: String,
        environment: String,
        targetAppIdKey: String,
        minSdk: Int,
        action: Action<AndroidBenchmarkExtension>,
    ) {
        configureTopLevelDsl {
            if (androidBenchmarkExtension == null) {
                androidBenchmarkExtension = objects.newInstance(
                    AndroidBenchmarkExtensionImpl::class.java,
                    namespace,
                    targetProjectPath,
                    environment,
                    targetAppIdKey,
                    minSdk,
                )
            }
            val extension = androidBenchmarkExtension!!
            action.execute(extension)
            extension.evaluate()
        }
    }

    private inline fun configureTopLevelDsl(block: () -> Unit) {
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
        AndroidCoreLibrary("androidCoreLibrary"),
        AndroidFeatureLibrary("androidFeatureLibrary"),
        AndroidApp("androidApp"),
        AndroidBenchmark("androidBenchmark"),
    }
}

@DslMarker
internal annotation class KStreamlinedExtensionMarker
