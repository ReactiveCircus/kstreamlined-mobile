package io.github.reactivecircus.v2p.gradle

import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.Property
import javax.inject.Inject

public interface V2PExtension {
    public fun generate(containerName: String, action: Action<CodegenOptions>)

    public fun runCodegenOnSync(variantName: String)

    public abstract class CodegenOptions @Inject constructor(objects: ObjectFactory) {
        public val prefix: Property<String> = objects.property(String::class.java)
        public val generateAsListFunction: Property<Boolean> = objects.property(Boolean::class.java).convention(false)
    }
}

internal abstract class V2PExtensionImpl @Inject constructor(private val objects: ObjectFactory) : V2PExtension {
    val groupsOfCodegenOptions = mutableMapOf<String, V2PExtension.CodegenOptions>()

    var variantForCodegenOnSync: String? = null
        private set

    override fun generate(containerName: String, action: Action<V2PExtension.CodegenOptions>) {
        groupsOfCodegenOptions.computeIfAbsent(containerName) {
            objects.newInstance(V2PExtension.CodegenOptions::class.java).also {
                action.execute(it)
            }
        }
    }

    override fun runCodegenOnSync(variantName: String) {
        variantForCodegenOnSync = variantName
    }
}
