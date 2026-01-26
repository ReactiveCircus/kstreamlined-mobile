package io.github.reactivecircus.routebinding.compiler.ir

import io.github.reactivecircus.routebinding.compiler.ClassIds
import io.github.reactivecircus.routebinding.compiler.ir.symbols.ComposeSymbols
import io.github.reactivecircus.routebinding.compiler.ir.symbols.MetroSymbols
import io.github.reactivecircus.routebinding.compiler.ir.symbols.Nav3Symbols
import io.github.reactivecircus.routebinding.compiler.ir.symbols.RouteBindingSymbols
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.IrBlockBodyBuilder
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irCallConstructor
import org.jetbrains.kotlin.ir.builders.irCallWithSubstitutedType
import org.jetbrains.kotlin.ir.builders.irDelegatingConstructorCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irGetObject
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.builders.irUnit
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.expressions.IrClassReference
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionExpressionImpl
import org.jetbrains.kotlin.ir.interpreter.getAnnotation
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.getClass
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.file
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.nestedClasses
import org.jetbrains.kotlin.ir.util.simpleFunctions
import org.jetbrains.kotlin.name.SpecialNames

internal class RouteBindingClassTransformer(
    private val pluginContext: IrPluginContext,
    private val routeBindingSymbols: RouteBindingSymbols,
    private val composeSymbols: ComposeSymbols,
    private val nav3Symbols: Nav3Symbols,
    private val metroSymbols: MetroSymbols,
) : IrElementTransformerVoidWithContext() {
    override fun visitClassNew(declaration: IrClass): IrStatement {
        if (declaration.origin == RouteBindingOrigins.NavEntryInstallerClassDeclaration) {
            transformNavEntryInstallerClass(declaration)
        }
        return super.visitClassNew(declaration)
    }

    private fun transformNavEntryInstallerClass(irClass: IrClass) {
        irClass.constructors.forEach { constructor ->
            constructor.body = DeclarationIrBuilder(pluginContext, constructor.symbol).irBlockBody {
                +irDelegatingConstructorCall(pluginContext.irBuiltIns.anyClass.owner.constructors.single())
            }
        }

        val installFunction = irClass.functions.first {
            it.origin == RouteBindingOrigins.InstallFunction
        }

        // TODO implement actual body that calls the original @RouteBinding function
        installFunction.body = DeclarationIrBuilder(pluginContext, installFunction.symbol).irBlockBody {
            +irUnit()
        }

        val metroFactoryClass = irClass.nestedClasses.find {
            it.origin == RouteBindingOrigins.MetroFallbacks.MetroFactoryClass
        }
        if (metroFactoryClass != null) {
            transformMetroFactoryClass(factoryClass = metroFactoryClass, parent = irClass)
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
                    // TODO
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

    /**
     * Add function bodies to synthetic MetroFactory class generated in FIR.
     * This only runs when Metro compiler is not on the classpath, i.e. during compiler tests.
     */
    private fun transformMetroFactoryClass(factoryClass: IrClass, parent: IrClass) {
        val createFunction = factoryClass.functions.single {
            it.origin == RouteBindingOrigins.MetroFallbacks.MetroFactoryCreateFunction
        }
        createFunction.body = DeclarationIrBuilder(pluginContext, createFunction.symbol).irBlockBody {
            +irReturn(irGetObject(factoryClass.symbol))
        }

        val newInstanceFunction = factoryClass.functions.single {
            it.origin == RouteBindingOrigins.MetroFallbacks.MetroFactoryNewInstanceFunction
        }
        newInstanceFunction.body = DeclarationIrBuilder(pluginContext, newInstanceFunction.symbol).irBlockBody {
            val parentConstructor = parent.constructors.first()
            +irReturn(irCallConstructor(parentConstructor.symbol, emptyList()))
        }
    }

    /**
     * Find the original @RouteBinding annotated function in the same file
     */
    private fun IrClass.findOriginalRouteBindingFunction(functionName: String): IrSimpleFunction? {
        return file.simpleFunctions()
            .find { function ->
                function.name.asString() == functionName &&
                    function.hasAnnotation(routeBindingSymbols.routeBindingAnnotation)
            }
    }
}
