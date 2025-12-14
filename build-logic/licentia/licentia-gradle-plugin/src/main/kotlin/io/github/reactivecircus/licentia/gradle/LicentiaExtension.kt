package io.github.reactivecircus.licentia.gradle

public interface LicentiaExtension {
    public fun runCodegenOnSync(variantName: String)
}

internal abstract class LicentiaExtensionImpl : LicentiaExtension {
    var variantForCodegenOnSync: String? = null
        private set

    override fun runCodegenOnSync(variantName: String) {
        variantForCodegenOnSync = variantName
    }
}
