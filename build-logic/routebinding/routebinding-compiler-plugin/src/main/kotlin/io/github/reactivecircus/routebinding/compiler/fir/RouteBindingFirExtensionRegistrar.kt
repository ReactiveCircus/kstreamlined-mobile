package io.github.reactivecircus.routebinding.compiler.fir

import io.github.reactivecircus.routebinding.compiler.fir.diagnostics.RouteBindingFirAdditionalCheckersExtension
import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

internal object RouteBindingFirExtensionRegistrar : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +::RouteBindingFirAdditionalCheckersExtension
    }
}
