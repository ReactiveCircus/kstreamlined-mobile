package io.github.reactivecircus.v2p.gradle

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import javax.inject.Inject

public interface V2PExtension {
    public fun generate(containerName: String, action: Action<V2PCodegenOptions>)

    public fun runCodegenOnSync(variantName: String)
}

internal abstract class V2PExtensionImpl @Inject constructor(private val objects: ObjectFactory) : V2PExtension {
    val codegenOptionsMap = mutableMapOf<String, V2PCodegenOptions>()

    var variantForCodegenOnSync: String? = null
        private set

    override fun generate(containerName: String, action: Action<V2PCodegenOptions>) {
        codegenOptionsMap.computeIfAbsent(containerName) {
            objects.newInstance(V2PCodegenOptionsImpl::class.java).also {
                action.execute(it)
            }
        }
    }

    override fun runCodegenOnSync(variantName: String) {
        variantForCodegenOnSync = variantName
    }
}

public interface V2PCodegenOptions {
    @get:Input
    public val prefix: Property<String>

    @get:Input
    public val generateAsListFunction: Property<Boolean>

    @get:Input
    @get:Optional
    public val subpackage: Property<String>
}

internal abstract class V2PCodegenOptionsImpl @Inject constructor(objects: ObjectFactory) : V2PCodegenOptions {
    override val prefix: Property<String> = objects.property(String::class.java)
    override val generateAsListFunction: Property<Boolean> = objects.property(Boolean::class.java).convention(false)
    override val subpackage: Property<String> = objects.property(String::class.java)
}
