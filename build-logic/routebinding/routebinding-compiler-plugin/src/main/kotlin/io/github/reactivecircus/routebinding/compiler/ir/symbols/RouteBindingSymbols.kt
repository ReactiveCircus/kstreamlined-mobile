@file:Suppress("ReturnCount")

package io.github.reactivecircus.routebinding.compiler.ir.symbols

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name

internal class RouteBindingSymbols private constructor(
    val routeBindingAnnotation: IrClassSymbol,
    val navEntryInstallerInterface: IrClassSymbol,
    val installFunction: IrSimpleFunctionSymbol,
) {
    companion object {
        fun create(
            pluginContext: IrPluginContext,
            messageCollector: MessageCollector,
            routeBindingAnnotationId: ClassId,
            navEntryInstallerClassId: ClassId,
        ): RouteBindingSymbols? {
            val routeBindingAnnotation = pluginContext.finderForBuiltins().findClass(routeBindingAnnotationId)
            if (routeBindingAnnotation == null) {
                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Could not find annotation class <$routeBindingAnnotationId>.",
                )
                return null
            }
            val navEntryInstallerInterface = pluginContext.finderForBuiltins().findClass(navEntryInstallerClassId)
            if (navEntryInstallerInterface == null) {
                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Could not find interface <$navEntryInstallerClassId>.",
                )
                return null
            }
            val installFunctionCallableId = CallableId(
                classId = navEntryInstallerClassId,
                callableName = Name.identifier("install"),
            )
            val installFunction = pluginContext.finderForBuiltins().findFunctions(
                installFunctionCallableId,
            ).singleOrNull()
            if (installFunction == null) {
                messageCollector.report(
                    CompilerMessageSeverity.ERROR,
                    "Could not find function <$installFunctionCallableId>.",
                )
                return null
            }

            return RouteBindingSymbols(
                routeBindingAnnotation = routeBindingAnnotation,
                navEntryInstallerInterface = navEntryInstallerInterface,
                installFunction = installFunction,
            )
        }
    }
}
