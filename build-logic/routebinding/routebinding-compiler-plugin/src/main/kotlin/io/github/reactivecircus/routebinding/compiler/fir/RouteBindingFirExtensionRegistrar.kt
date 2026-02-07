package io.github.reactivecircus.routebinding.compiler.fir

import org.jetbrains.kotlin.fir.extensions.FirExtensionRegistrar

internal object RouteBindingFirExtensionRegistrar : FirExtensionRegistrar() {
    override fun ExtensionRegistrarContext.configurePlugin() {
        +::NavEntryInstallerClassGenerator
    }
}
