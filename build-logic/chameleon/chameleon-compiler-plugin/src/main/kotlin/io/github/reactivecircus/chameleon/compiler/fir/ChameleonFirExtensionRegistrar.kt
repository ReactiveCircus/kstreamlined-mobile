package io.github.reactivecircus.chameleon.compiler.fir

import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

internal object ChameleonFirExtensionRegistrar : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +::ChameleonFirAdditionalCheckersExtension
    }
}
