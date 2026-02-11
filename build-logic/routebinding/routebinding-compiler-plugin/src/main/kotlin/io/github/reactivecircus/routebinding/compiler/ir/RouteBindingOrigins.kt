package io.github.reactivecircus.routebinding.compiler.ir

import io.github.reactivecircus.routebinding.compiler.fir.RouteBindingKeys
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin

internal object RouteBindingOrigins {
    val NavEntryInstallerClassDeclaration: IrDeclarationOrigin = IrDeclarationOrigin.GeneratedByPlugin(
        RouteBindingKeys.NavEntryInstallerClassDeclaration,
    )

    val InstallFunction: IrDeclarationOrigin = IrDeclarationOrigin.GeneratedByPlugin(
        RouteBindingKeys.InstallFunctionDeclaration,
    )

    /**
     * Fallback MetroFactory origins when Metro compiler is not on the classpath.
     * These are only used by RouteBinding IR to generate `MetroFactory`'s function bodies during compiler tests.
     */
    object MetroFallbacks {
        val MetroFactoryClass: IrDeclarationOrigin by lazy {
            IrDeclarationOrigin.GeneratedByPlugin(RouteBindingKeys.MetroFallbacks.MetroFactoryClassDeclaration)
        }

        val MetroFactoryCreateFunction: IrDeclarationOrigin by lazy {
            IrDeclarationOrigin.GeneratedByPlugin(RouteBindingKeys.MetroFallbacks.MetroFactoryCreateFunction)
        }

        val MetroFactoryNewInstanceFunction: IrDeclarationOrigin by lazy {
            IrDeclarationOrigin.GeneratedByPlugin(RouteBindingKeys.MetroFallbacks.MetroFactoryNewInstanceFunction)
        }
    }
}
