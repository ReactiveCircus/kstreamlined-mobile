package io.github.reactivecircus.routebinding.compiler.ir

import io.github.reactivecircus.routebinding.compiler.ClassIds
import io.github.reactivecircus.routebinding.compiler.ir.symbols.Nav3Symbols
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
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irCallConstructor
import org.jetbrains.kotlin.ir.builders.irCallWithSubstitutedType
import org.jetbrains.kotlin.ir.builders.irDelegatingConstructorCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irGetObject
import org.jetbrains.kotlin.ir.builders.irReturn
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
import org.jetbrains.kotlin.ir.util.classIdOrFail
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.functions
import org.jetbrains.kotlin.ir.util.getPackageFragment
import org.jetbrains.kotlin.ir.util.hasDefaultValue
import org.jetbrains.kotlin.ir.util.nestedClasses
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames

internal class NavEntryInstallerClassTransformer(
    private val pluginContext: IrPluginContext,
    private val nav3Symbols: Nav3Symbols,
    routeBindingFunctions: Sequence<IrSimpleFunction>,
) : IrElementTransformerVoidWithContext() {
    // generated NavEntryInstaller class -> source function mappings
    private val sourceFunctionsMap: Map<ClassId, IrSimpleFunction> = routeBindingFunctions
        .associateBy { function ->
            val classNameSuffix = ClassIds.RouteBinding.NavEntryInstaller.shortClassName.asString()
            ClassId(
                function.getPackageFragment().packageFqName,
                Name.identifier("${function.name.asString()}_$classNameSuffix"),
            )
        }

    override fun visitClassNew(declaration: IrClass): IrStatement {
        if (declaration.origin == RouteBindingOrigins.NavEntryInstallerClassDeclaration) {
            val sourceFunction = sourceFunctionsMap.getValue(declaration.classIdOrFail)
            transformNavEntryInstallerClass(irClass = declaration, sourceFunction = sourceFunction)
        }
        return super.visitClassNew(declaration)
    }

    private fun transformNavEntryInstallerClass(irClass: IrClass, sourceFunction: IrSimpleFunction) {
        irClass.constructors.forEach { constructor ->
            constructor.body = DeclarationIrBuilder(pluginContext, constructor.symbol).irBlockBody {
                +irDelegatingConstructorCall(pluginContext.irBuiltIns.anyClass.owner.constructors.single())
            }
        }

        val installFunction = irClass.functions.first {
            it.origin == RouteBindingOrigins.InstallFunction
        }

        installFunction.body = DeclarationIrBuilder(pluginContext, installFunction.symbol).irBlockBody {
            createInstallFunctionBody(
                sourceFunction = sourceFunction,
                installFunction = installFunction,
            )
        }

        val metroFactoryClass = irClass.nestedClasses.find {
            it.origin == RouteBindingOrigins.MetroFallbacks.MetroFactoryClass
        }
        if (metroFactoryClass != null) {
            transformMetroFactoryClass(factoryClass = metroFactoryClass, parent = irClass)
        }
    }

    private fun IrBlockBodyBuilder.createInstallFunctionBody(
        sourceFunction: IrFunction,
        installFunction: IrFunction,
    ) {
        val routeKClass = sourceFunction.getAnnotation(
            ClassIds.RouteBinding.Annotation.asSingleFqName(),
        ).arguments.first() as IrClassReference
        val routeType = routeKClass.symbol.defaultType
        val entryFunction = nav3Symbols.entryFunction

        // TODO investigate why the following causes runtime crash:
        //  java.lang.VerifyError: Verifier rejected class io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.impl.TalkingKotlinEpisodeScreen_NavEntryInstaller$$ExternalSyntheticLambda0: java.lang.Object io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.impl.TalkingKotlinEpisodeScreen_NavEntryInstaller$$ExternalSyntheticLambda0.invoke(java.lang.Object, java.lang.Object, java.lang.Object) failed to verify: java.lang.Object io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.impl.TalkingKotlinEpisodeScreen_NavEntryInstaller$$ExternalSyntheticLambda0.invoke(java.lang.Object, java.lang.Object, java.lang.Object): [0xC] register v3 has type Reference java.lang.Object but expected Reference: io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.api.TalkingKotlinEpisodeRoute (declaration of 'io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.impl.TalkingKotlinEpisodeScreen_NavEntryInstaller$$ExternalSyntheticLambda0' appears in /data/app/~~0UcmCu8jDQGX8N8_ZVr8Fg==/io.github.reactivecircus.kstreamlined.dev-mLSl3MjImjN-ugmPyH46Hg==/base.apk!classes11.dex)
        +irCallWithSubstitutedType(entryFunction, listOf(routeType)).apply {
            val entryProviderScopeParam = installFunction.parameters.first {
                it.type.getClass()?.classId == ClassIds.Nav3.EntryProviderScope
            }
            dispatchReceiver = irGet(entryProviderScopeParam)

            val entryFunctionParams = entryFunction.owner.parameters
            val lambdaExpression = pluginContext.createLambdaIrFunctionExpression(
                lambdaReturnType = entryFunctionParams.last().type,
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
                                arguments[index] = irGet(installFunction.parameters.first { it.type == parameter.type })
                            }
                        }
                    }
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
}
