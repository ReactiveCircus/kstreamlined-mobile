@file:Suppress("ReturnCount")

package io.github.reactivecircus.routebinding.compiler.ir

import io.github.reactivecircus.routebinding.compiler.ir.symbols.ComposeSymbols
import io.github.reactivecircus.routebinding.compiler.ir.symbols.MetroSymbols
import io.github.reactivecircus.routebinding.compiler.ir.symbols.Nav3Symbols
import io.github.reactivecircus.routebinding.compiler.ir.symbols.RouteBindingSymbols
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.declarations.buildClass
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.expressions.IrAnnotation
import org.jetbrains.kotlin.ir.expressions.impl.IrAnnotationImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrClassReferenceImpl
import org.jetbrains.kotlin.ir.expressions.impl.fromSymbolOwner
import org.jetbrains.kotlin.ir.types.IrTypeSystemContextImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.addChild
import org.jetbrains.kotlin.ir.util.addFakeOverrides
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.createThisReceiverParameter
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.name.Name

internal class RouteBindingFunctionTransformer(
    private val pluginContext: IrPluginContext,
    private val routeBindingSymbols: RouteBindingSymbols,
    private val composeSymbols: ComposeSymbols,
    private val nav3Symbols: Nav3Symbols,
    private val metroSymbols: MetroSymbols,
) : IrElementTransformerVoidWithContext() {
    override fun visitFunctionNew(declaration: IrFunction): IrStatement {
        // skip transform if function isn't annotated with @RouteBinding
        if (!declaration.hasAnnotation(routeBindingSymbols.routeBindingAnnotation)) {
            return super.visitFunctionNew(declaration)
        }

        val navEntryInstallerClass = createNavEntryInstallerClass(declaration, currentFile)
        currentFile.addChild(navEntryInstallerClass)

        return super.visitFunctionNew(declaration)
    }

    private fun createNavEntryInstallerClass(
        function: IrFunction,
        file: IrFile,
    ): IrClass {
        return pluginContext.irFactory.buildClass {
            startOffset = UNDEFINED_OFFSET
            endOffset = UNDEFINED_OFFSET
            origin = IrDeclarationOrigin.DEFINED
            name = Name.identifier("${function.name.asString()}_NavEntryInstaller")
            kind = ClassKind.CLASS
            modality = Modality.FINAL
            visibility = DescriptorVisibilities.PUBLIC
        }.apply {
            parent = file
            superTypes = listOf(routeBindingSymbols.navEntryInstallerInterface.defaultType)
            annotations += createMetroAnnotation()
            createThisReceiverParameter()
            createDefaultConstructor(pluginContext)
            addFakeOverrides(IrTypeSystemContextImpl(pluginContext.irBuiltIns))
        }
    }

    private fun createMetroAnnotation(): IrAnnotation {
        return IrAnnotationImpl.fromSymbolOwner(
            type = metroSymbols.contributesIntoSetAnnotation.defaultType,
            constructorSymbol = metroSymbols.contributesIntoSetAnnotation.owner.constructors.first().symbol,
        ).also {
            val appScopeClassSymbol = metroSymbols.appScopeAnnotation
            it.arguments[0] = IrClassReferenceImpl(
                startOffset = UNDEFINED_OFFSET,
                endOffset = UNDEFINED_OFFSET,
                type = pluginContext.irBuiltIns.kClassClass.typeWith(appScopeClassSymbol.defaultType),
                symbol = appScopeClassSymbol,
                classType = appScopeClassSymbol.defaultType,
            )
        }
    }
}
