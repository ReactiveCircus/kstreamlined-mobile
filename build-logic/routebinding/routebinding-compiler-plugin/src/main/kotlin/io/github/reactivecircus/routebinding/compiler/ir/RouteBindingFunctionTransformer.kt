@file:Suppress("ReturnCount")

package io.github.reactivecircus.routebinding.compiler.ir

import io.github.reactivecircus.routebinding.compiler.ir.symbols.ComposeSymbols
import io.github.reactivecircus.routebinding.compiler.ir.symbols.MetroSymbols
import io.github.reactivecircus.routebinding.compiler.ir.symbols.Nav3Symbols
import io.github.reactivecircus.routebinding.compiler.ir.symbols.RouteBindingSymbols
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.declarations.addFunction
import org.jetbrains.kotlin.ir.builders.declarations.buildClass
import org.jetbrains.kotlin.ir.builders.declarations.buildValueParameter
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrParameterKind
import org.jetbrains.kotlin.ir.expressions.IrAnnotation
import org.jetbrains.kotlin.ir.expressions.impl.IrAnnotationImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrClassReferenceImpl
import org.jetbrains.kotlin.ir.expressions.impl.fromSymbolOwner
import org.jetbrains.kotlin.ir.types.IrTypeSystemContextImpl
import org.jetbrains.kotlin.ir.types.createType
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.addChild
import org.jetbrains.kotlin.ir.util.addFakeOverrides
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.createThisReceiverParameter
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames

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

        val navEntryInstallerClass = declaration.createNavEntryInstallerClass(currentFile)
        currentFile.addChild(navEntryInstallerClass)

        return super.visitFunctionNew(declaration)
    }

    private fun IrFunction.createNavEntryInstallerClass(file: IrFile): IrClass {
        return pluginContext.irFactory.buildClass {
            startOffset = UNDEFINED_OFFSET
            endOffset = UNDEFINED_OFFSET
            origin = IrDeclarationOrigin.DEFINED
            name = Name.identifier("${this@createNavEntryInstallerClass.name.asString()}_NavEntryInstaller")
            kind = ClassKind.CLASS
            modality = Modality.FINAL
            visibility = DescriptorVisibilities.PUBLIC
        }.apply {
            parent = file
            superTypes = listOf(routeBindingSymbols.navEntryInstallerInterface.defaultType)
            annotations += createMetroAnnotation()
            createThisReceiverParameter()
            createDefaultConstructor(pluginContext)
            addInstallFunction()
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

    private fun IrClass.addInstallFunction() = addFunction {
        name = Name.identifier("install")
        visibility = DescriptorVisibilities.PUBLIC
        modality = Modality.OPEN
        returnType = pluginContext.irBuiltIns.unitType
    }.apply {
        parameters = listOf(
            buildValueParameter(this) {
                name = SpecialNames.THIS
                type = this@addInstallFunction.defaultType
                kind = IrParameterKind.DispatchReceiver
            },
            buildValueParameter(this) {
                name = Name.identifier("entryProviderScope")
                type = nav3Symbols.entryProviderScopeClass.createType(
                    hasQuestionMark = false,
                    arguments = listOf(nav3Symbols.navKeyInterface.defaultType),
                )
                kind = IrParameterKind.Context
            },
            buildValueParameter(this) {
                name = Name.identifier("sharedTransitionScope")
                type = composeSymbols.sharedTransitionScopeInterface.defaultType
                kind = IrParameterKind.Context
            },
            buildValueParameter(this) {
                name = Name.identifier("backStack")
                type = nav3Symbols.navBackStackClass.createType(
                    hasQuestionMark = false,
                    arguments = listOf(nav3Symbols.navKeyInterface.defaultType),
                )
                kind = IrParameterKind.Regular
            },
        )
        overriddenSymbols = listOf(routeBindingSymbols.installFunction)
        body = DeclarationIrBuilder(
            generatorContext = pluginContext,
            symbol = symbol,
            startOffset = symbol.owner.startOffset,
            endOffset = symbol.owner.endOffset,
        ).irBlockBody {
        }
    }
}
