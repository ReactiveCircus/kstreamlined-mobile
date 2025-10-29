package io.github.reactivecircus.chameleon.compiler

import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.declarations.addBackingField
import org.jetbrains.kotlin.ir.builders.declarations.addProperty
import org.jetbrains.kotlin.ir.builders.declarations.addValueParameter
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.createExpressionBody
import org.jetbrains.kotlin.ir.expressions.IrCall
import org.jetbrains.kotlin.ir.expressions.IrExpression
import org.jetbrains.kotlin.ir.expressions.IrStatementOrigin
import org.jetbrains.kotlin.ir.expressions.impl.IrCallImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.types.defaultType
import org.jetbrains.kotlin.ir.types.getClass
import org.jetbrains.kotlin.ir.util.callableId
import org.jetbrains.kotlin.ir.util.classId
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.util.getArgumentsWithIr
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.primaryConstructor
import org.jetbrains.kotlin.ir.util.properties
import org.jetbrains.kotlin.name.Name

internal class ChameleonClassTransformer(
    private val pluginContext: IrPluginContext,
    private val messageCollector: MessageCollector, // TODO remove
    private val chameleonSymbols: ChameleonSymbols,
) : IrElementTransformerVoidWithContext() {
    override fun visitClassNew(declaration: IrClass): IrStatement {
        // skip transform if class isn't annotated with chameleon annotation
        if (!declaration.hasAnnotation(chameleonSymbols.chameleonAnnotation)) return super.visitClassNew(declaration)

        // skip transform if class already has theme variant property
        if (declaration.findThemeVariantProperty() != null) return super.visitClassNew(declaration)

        log("Transforming class: ${declaration.name}")
        declaration.transform(
            SnapshotCallTransformer(
                themeVariantProperty = { declaration.getOrCreateThemeVariantProperty() }
            ), null)

        // move the added themeVariant property to the top of the declarations after the constructors
        val themeVariantProperty = declaration.findThemeVariantProperty()
        if (themeVariantProperty != null) {
            declaration.declarations.remove(themeVariantProperty)
            val newIndex = declaration.declarations.indexOfLast { it is IrConstructor } + 1
            declaration.declarations.add(newIndex, themeVariantProperty)
        }

        log("Transformed class IR: ${declaration.dump()}")
        return super.visitClassNew(declaration)
    }

    private fun IrClass.getOrCreateThemeVariantProperty(): IrProperty {
        val existing = findThemeVariantProperty()
        if (existing != null) return existing

        val propertyName = Name.identifier("themeVariant")
        val propertyType = chameleonSymbols.themeVariantEnum.defaultType
        val ctorParam = primaryConstructor?.addValueParameter {
            name = propertyName
            type = propertyType
            origin = IrDeclarationOrigin.DEFINED
        } ?: error("Missing primary constructor from class '${name.asString()}'.")

        return addProperty {
            name = propertyName
            visibility = DescriptorVisibilities.PRIVATE
            modality = Modality.FINAL
        }.apply {
            backingField = addBackingField {
                type = propertyType
                isFinal = true
            }.apply {
                initializer = factory.createExpressionBody(
                    IrGetValueImpl(
                        startOffset = UNDEFINED_OFFSET,
                        endOffset = UNDEFINED_OFFSET,
                        type = propertyType,
                        symbol = ctorParam.symbol,
                        origin = IrStatementOrigin.INITIALIZE_PROPERTY_FROM_PARAMETER,
                    )
                )
            }
            val irClass = this@getOrCreateThemeVariantProperty
            addDefaultGetterWithSameVisibility(irClass, pluginContext.irBuiltIns)
        }
    }

    private fun IrClass.findThemeVariantProperty(): IrProperty? = properties.firstOrNull {
        it.backingField?.type?.getClass()?.classId == chameleonSymbols.themeVariantEnum.owner.classId
    }

    private inner class SnapshotCallTransformer(
        private val themeVariantProperty: () -> IrProperty
    ) : IrElementTransformerVoidWithContext() {
        override fun visitCall(expression: IrCall): IrExpression {
            // skip if not snapshot function call
            if (expression.symbol.owner.callableId != chameleonSymbols.snapshotFunction.owner.callableId) {
                return super.visitCall(expression)
            }
            // skip if themeVariant argument is already provided
            if (expression.getArgumentsWithIr().any { it.first.type.getClass()?.classId == chameleonSymbols.themeVariantEnum.owner.classId }) {
                return super.visitCall(expression)
            }

            // get the parent class's themeVariant property
            val themeVariantProperty = themeVariantProperty()

            // find the themeVariant parameter from the snapshot function
            val themeVariantParam = expression.symbol.owner.parameters.first {
                it.type.getClass()?.classId == chameleonSymbols.themeVariantEnum.owner.classId
            }

            // get current function's dispatcher receiver
            val dispatchReceiver = (currentFunction?.irElement as? IrFunction)?.dispatchReceiverParameter ?: return super.visitCall(expression)
            val dispatchReceiverGet = IrGetValueImpl(
                startOffset = expression.startOffset,
                endOffset = expression.endOffset,
                type = dispatchReceiver.type,
                symbol = dispatchReceiver.symbol,
                origin = IrStatementOrigin.IMPLICIT_ARGUMENT
            )

            // create a call to the property getter
            val propertyGetterCall = IrCallImpl(
                expression.startOffset,
                expression.endOffset,
                themeVariantProperty.getter!!.returnType,
                themeVariantProperty.getter!!.symbol,
                typeArgumentsCount = 0,
                origin = IrStatementOrigin.GET_PROPERTY
            ).apply {
                arguments[0] = dispatchReceiverGet
            }

            // set the argument at the parameter's index
            expression.arguments[themeVariantParam.indexInParameters] = propertyGetterCall

            return super.visitCall(expression)
        }
    }

    private fun log(message: String) {
        messageCollector.report(CompilerMessageSeverity.LOGGING, "Chameleon Compiler Plugin (IR) - $message")
    }
}
