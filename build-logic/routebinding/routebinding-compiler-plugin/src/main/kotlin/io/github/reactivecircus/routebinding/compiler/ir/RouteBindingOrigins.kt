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
}
