@file:Suppress("ReturnCount")

package io.github.reactivecircus.routebinding.compiler.ir

import io.github.reactivecircus.routebinding.compiler.ClassIds
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
import org.jetbrains.kotlin.ir.builders.IrBlockBodyBuilder
import org.jetbrains.kotlin.ir.builders.declarations.addFunction
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.declarations.buildClass
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.builders.declarations.buildValueParameter
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irCallWithSubstitutedType
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFile
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrParameterKind
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrAnnotation
import org.jetbrains.kotlin.ir.expressions.IrClassReference
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrAnnotationImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrClassReferenceImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionExpressionImpl
import org.jetbrains.kotlin.ir.expressions.impl.fromSymbolOwner
import org.jetbrains.kotlin.ir.interpreter.getAnnotation
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.IrTypeSystemContextImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.getClass
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.addChild
import org.jetbrains.kotlin.ir.util.addFakeOverrides
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.copyFunctionSignatureFrom
import org.jetbrains.kotlin.ir.util.createThisReceiverParameter
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.dump
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
        if (!declaration.hasAnnotation(routeBindingSymbols.routeBindingAnnotation)) {
            return super.visitFunctionNew(declaration)
        }

//        val navEntryInstallerClass = declaration.createNavEntryInstallerClass(currentFile)
//        currentFile.addChild(navEntryInstallerClass)

        return super.visitFunctionNew(declaration)
    }

    private fun IrFunction.createNavEntryInstallerClass(file: IrFile): IrClass {
        return factory.buildClass {
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
            createInstallFunction(navEntryContentFunction = this@createNavEntryInstallerClass)
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

    private fun IrClass.createInstallFunction(navEntryContentFunction: IrFunction) = addFunction {
        name = Name.identifier("install")
        modality = Modality.OPEN
    }.apply {
        overriddenSymbols = listOf(routeBindingSymbols.installFunction)
        copyFunctionSignatureFrom(routeBindingSymbols.installFunction.owner)
        parameters = listOf(
            buildValueParameter(this) {
                name = SpecialNames.THIS
                type = this@createInstallFunction.defaultType
                kind = IrParameterKind.DispatchReceiver
            },
        ) + parameters.dropWhile { it.kind == IrParameterKind.DispatchReceiver }
        body = DeclarationIrBuilder(pluginContext, symbol).irBlockBody {
            createInstallFunctionBody(navEntryContentFunction = navEntryContentFunction, installFunction = this@apply)
        }
    }

    private fun IrBlockBodyBuilder.createInstallFunctionBody(
        navEntryContentFunction: IrFunction,
        installFunction: IrFunction,
    ) {
        val routeKClass = navEntryContentFunction.getAnnotation(
            ClassIds.RouteBinding.Annotation.asSingleFqName(),
        ).arguments.first() as IrClassReference
        val routeType = routeKClass.symbol.defaultType
        val entryFunction = nav3Symbols.entryFunction

        +irCallWithSubstitutedType(entryFunction, listOf(routeType)).apply {
            val sharedTransitionScopeParam = installFunction.parameters.first {
                it.type.getClass()?.classId == ClassIds.Nav3.EntryProviderScope
            }
            dispatchReceiver = irGet(sharedTransitionScopeParam)

            val entryFunctionParams = entryFunction.owner.parameters
            val lambdaExpression = pluginContext.createLambdaIrFunctionExpression(
                lambdaReturnType = entryFunctionParams.last().type,
            ) {
                parent = installFunction
                addValueParameter(name = "it", type = routeType)
                body = DeclarationIrBuilder(pluginContext, symbol).irBlockBody {

                }
            }
            arguments[entryFunctionParams.size - 1] = lambdaExpression
        }
    }

    private fun IrPluginContext.createLambdaIrFunctionExpression(
        lambdaReturnType: IrType,
        block: IrSimpleFunction.() -> Unit = {},
    ): IrExpression {
        val lambda = irFactory.buildFun {
            name = SpecialNames.ANONYMOUS
            origin = IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA
            visibility = DescriptorVisibilities.LOCAL
            returnType = lambdaReturnType
        }.apply(block)

        return IrFunctionExpressionImpl(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            type = lambda.returnType,
            function = lambda,
            origin = IrStatementOrigin.LAMBDA,
        )
    }
}
