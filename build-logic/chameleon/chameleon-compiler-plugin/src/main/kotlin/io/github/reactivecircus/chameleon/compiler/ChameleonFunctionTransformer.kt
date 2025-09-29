package io.github.reactivecircus.chameleon.compiler

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.builders.irBlock
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.createBlockBody
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrFunctionExpressionImpl
import org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI
import org.jetbrains.kotlin.ir.types.IrType
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.statements
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.SpecialNames

internal class ChameleonFunctionTransformer(
    private val pluginContext: IrPluginContext,
    private val messageCollector: MessageCollector,
    private val chameleonAnnotation: ClassId,
    private val snapshotFunction: CallableId,
    private val themeVariantEnum: ClassId,
) : IrElementTransformerVoidWithContext() {
    @OptIn(UnsafeDuringIrConstructionAPI::class)
    override fun visitFunctionNew(declaration: IrFunction): IrStatement {
        if (!declaration.hasAnnotation(chameleonAnnotation) || declaration.body == null) {
            return super.visitFunctionNew(declaration)
        }

        // TODO check for $composer and report error

        val originalBody = declaration.body!!

        declaration.body = pluginContext.irFactory.createBlockBody(
            startOffset = originalBody.startOffset,
            endOffset = originalBody.endOffset,
        ).apply {
            val snapshotFunction = pluginContext.referenceFunctions(snapshotFunction).single()
            val irBuilder = DeclarationIrBuilder(pluginContext, declaration.symbol)

            // TODO move up and check early:
            //  - must have at least 1 param
            //  - last must be kotlin.Function0<kotlin.Unit>)
            //  - move to FIR?
            val snapshotFunctionParameters = snapshotFunction.owner.parameters

            statements.add(
                irBuilder.irBlock {
                    +irCall(snapshotFunction).apply {
                        val lambdaExpression = pluginContext.createLambdaIrFunctionExpression(
                            lambdaReturnType = snapshotFunctionParameters.last().type,
                        ) {
                            parent = declaration
                            body = pluginContext.irFactory.createBlockBody(
                                startOffset,
                                endOffset,
                                originalBody.statements,
                            )
                        }
                        arguments[snapshotFunctionParameters.size - 1] = lambdaExpression
                    }
                },
            )
        }

        log("Transformed function IR: \n${declaration.dump()}")

        return super.visitFunctionNew(declaration)
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

    private fun log(message: String) {
        messageCollector.report(CompilerMessageSeverity.LOGGING, "Chameleon Compiler Plugin (IR) - $message")
    }
}
