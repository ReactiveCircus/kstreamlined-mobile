package io.github.reactivecircus.routebinding.compiler.ir

import io.github.reactivecircus.routebinding.compiler.ClassIds
import io.github.reactivecircus.routebinding.compiler.ir.symbols.MetroSymbols
import io.github.reactivecircus.routebinding.compiler.ir.symbols.Nav3Symbols
import io.github.reactivecircus.routebinding.compiler.ir.symbols.RouteBindingSymbols
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.IrBlockBodyBuilder
import org.jetbrains.kotlin.ir.builders.declarations.addFunction
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.declarations.buildClass
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irCallWithSubstitutedType
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irGetObject
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFunction
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
import org.jetbrains.kotlin.ir.types.IrTypeSubstitutor
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.getClass
import org.jetbrains.kotlin.ir.types.impl.makeTypeProjection
import org.jetbrains.kotlin.ir.types.typeWith
import org.jetbrains.kotlin.ir.util.addChild
import org.jetbrains.kotlin.ir.util.addSimpleDelegatingConstructor
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.copyParametersFrom
import org.jetbrains.kotlin.ir.util.createThisReceiverParameter
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.file
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.hasDefaultValue
import org.jetbrains.kotlin.ir.util.parentAsClass
import org.jetbrains.kotlin.ir.util.primaryConstructor
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames
import org.jetbrains.kotlin.types.Variance

/**
 * Generates a class that implements `NavEntryInstaller` for each `@RouteBinding`-annotated source function.
 * The class is annotated with `@ContributesIntoSet(scope = AppScope::class)`,
 * the overridden `install` function invokes the source `@RouteBinding`-annotated function.
 */
internal class RouteBindingClassGenerator(
    private val pluginContext: IrPluginContext,
    private val metroSymbols: MetroSymbols,
    private val nav3Symbols: Nav3Symbols,
    routeBindingSymbols: RouteBindingSymbols,
) {
    private val navEntryInstallerSymbol = routeBindingSymbols.navEntryInstallInterface

    private val installFunction: IrSimpleFunction = navEntryInstallerSymbol.owner.functions
        .single { it.name.asString() == "install" }

    fun generate(sourceFunction: IrSimpleFunction) {
        val irClass = pluginContext.irFactory.buildClass {
            startOffset = UNDEFINED_OFFSET
            endOffset = UNDEFINED_OFFSET
            name = Name.identifier("${sourceFunction.name.asString()}_NavEntryInstaller")
            visibility = DescriptorVisibilities.PUBLIC
        }.apply {
            createThisReceiverParameter()
            superTypes = listOf(navEntryInstallerSymbol.owner.defaultType)
            annotations = listOf(buildContributesIntoSetAnnotation())
        }

        sourceFunction.file.addChild(irClass)
        pluginContext.metadataDeclarationRegistrar.registerClassAsMetadataVisible(irClass)

        addPrimaryConstructor(irClass)
        addInstallFunction(irClass, sourceFunction)
    }

    /**
     * Builds the `@ContributesIntoSet(scope = AppScope::class)` annotation.
     */
    private fun buildContributesIntoSetAnnotation(): IrAnnotation {
        val contributesIntoSetCtor = metroSymbols.contributesIntoSetClass.constructors.single()
        val appScopeSymbol = metroSymbols.appScopeClass
        val annotationType = contributesIntoSetCtor.owner.parentAsClass.defaultType
        val appScopeKClassType = pluginContext.irBuiltIns.kClassClass.typeWith(appScopeSymbol.owner.defaultType)
        return IrAnnotationImpl.fromSymbolOwner(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            type = annotationType,
            constructorSymbol = contributesIntoSetCtor,
        ).apply {
            // ContributesIntoSet(scope = AppScope::class, ...); remaining params have defaults.
            arguments[0] = IrClassReferenceImpl(
                startOffset = UNDEFINED_OFFSET,
                endOffset = UNDEFINED_OFFSET,
                type = appScopeKClassType,
                symbol = appScopeSymbol,
                classType = appScopeSymbol.owner.defaultType,
            )
        }
    }

    private fun addPrimaryConstructor(irClass: IrClass) {
        val anyConstructor = pluginContext.irBuiltIns.anyClass.owner.primaryConstructor!!
        val constructor = irClass.addSimpleDelegatingConstructor(
            superConstructor = anyConstructor,
            irBuiltIns = pluginContext.irBuiltIns,
            isPrimary = true,
        ).apply {
            visibility = DescriptorVisibilities.PRIVATE
        }
        pluginContext.metadataDeclarationRegistrar.registerConstructorAsMetadataVisible(constructor)
    }

    private fun addInstallFunction(irClass: IrClass, sourceFunction: IrSimpleFunction) {
        val function = irClass.addFunction {
            startOffset = UNDEFINED_OFFSET
            endOffset = UNDEFINED_OFFSET
            name = installFunction.name
            returnType = installFunction.returnType
            visibility = DescriptorVisibilities.PUBLIC
        }
        function.copyParametersFrom(installFunction)
        function.overriddenSymbols = listOf(installFunction.symbol)
        function.body = DeclarationIrBuilder(pluginContext, function.symbol).irBlockBody {
            createInstallFunctionBody(
                sourceFunction = sourceFunction,
                installFunction = function,
            )
        }
        pluginContext.metadataDeclarationRegistrar.registerFunctionAsMetadataVisible(function)
    }

    private fun IrBlockBodyBuilder.createInstallFunctionBody(
        sourceFunction: IrFunction,
        installFunction: IrFunction,
    ) {
        val annotation = sourceFunction.getAnnotation(ClassIds.RouteBinding.Annotation.asSingleFqName())
        val routeKClass = annotation.arguments.first() as IrClassReference
        val routeType = routeKClass.symbol.defaultType
        val entryFunction = nav3Symbols.entryFunction

        val metadataProviderKClass = annotation.arguments.getOrNull(1) as? IrClassReference
        val metadataProviderClass = metadataProviderKClass?.symbol?.owner as? IrClass
        val hasMetadataProvider = metadataProviderClass != null

        +irCallWithSubstitutedType(entryFunction, listOf(routeType)).apply {
            val entryProviderScopeParam = installFunction.parameters.first {
                it.type.getClass()?.classId == ClassIds.Nav3.EntryProviderScope
            }
            dispatchReceiver = irGet(entryProviderScopeParam)

            val entryFunctionParams = entryFunction.owner.parameters

            // pass metadata to entry function if a metadataProvider is specified
            if (hasMetadataProvider) {
                val provideFunction = metadataProviderClass.functions.first {
                    it.name == Name.identifier("provide")
                }
                val metadataParamIndex = entryFunctionParams.indexOfFirst {
                    it.name == Name.identifier("metadata")
                }
                arguments[metadataParamIndex] = irCall(provideFunction).apply {
                    dispatchReceiver = irGetObject(metadataProviderClass.symbol)
                }
            }

            val contentParamType = entryFunctionParams.last().type
            val lambdaFunctionType = IrTypeSubstitutor(
                typeParameters = entryFunction.owner.typeParameters.map { it.symbol },
                typeArguments = listOf(makeTypeProjection(routeType, Variance.INVARIANT)),
            ).substitute(contentParamType)

            val lambdaExpression = pluginContext.createLambdaIrFunctionExpression(
                lambdaFunctionType = lambdaFunctionType,
            ) {
                parent = installFunction
                val itParam = addValueParameter(name = "it", type = routeType)
                body = DeclarationIrBuilder(pluginContext, symbol).irBlockBody {
                    +irCall(sourceFunction.symbol).apply {
                        sourceFunction.parameters.forEachIndexed { index, parameter ->
                            if (parameter.hasDefaultValue()) return@forEachIndexed
                            if (parameter.type == routeType) {
                                arguments[index] = irGet(itParam)
                            } else {
                                arguments[index] = irGet(
                                    installFunction.parameters.single { it.type == parameter.type },
                                )
                            }
                        }
                    }
                }
            }
            arguments[entryFunctionParams.size - 1] = lambdaExpression
        }
    }

    private fun IrPluginContext.createLambdaIrFunctionExpression(
        lambdaFunctionType: IrType,
        block: IrSimpleFunction.() -> Unit = {},
    ): IrExpression {
        val lambda = irFactory.buildFun {
            name = SpecialNames.ANONYMOUS
            origin = IrDeclarationOrigin.LOCAL_FUNCTION_FOR_LAMBDA
            visibility = DescriptorVisibilities.LOCAL
            returnType = irBuiltIns.unitType
        }.apply(block)

        return IrFunctionExpressionImpl(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            type = lambdaFunctionType,
            function = lambda,
            origin = IrStatementOrigin.LAMBDA,
        )
    }
}
