package io.github.reactivecircus.chameleon.compiler

import org.jetbrains.kotlin.ir.IrBuiltIns
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.declarations.addGetter
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrDeclarationOrigin
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.declarations.createBlockBody
import org.jetbrains.kotlin.ir.expressions.impl.IrGetFieldImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrGetValueImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrReturnImpl
import org.jetbrains.kotlin.ir.util.copyTo
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

internal fun String.toMemberCallableId(): CallableId {
    val lastDotIndex = lastIndexOf('.')
    require(lastDotIndex != -1) { "No member callable name found in: $this" }

    val callableNameComponent = substring(lastDotIndex + 1)
    val packageAndClassComponent = substring(0, lastDotIndex)

    val segments = packageAndClassComponent.split('/')
    val className = segments.last()
    val packageSegments = segments.dropLast(1)

    val callableName = Name.identifier(callableNameComponent)
    val packageFqName = FqName.fromSegments(packageSegments)
    val classFqName = if (className.isNotEmpty()) FqName(className) else null

    return CallableId(packageFqName, classFqName, callableName)
}

internal fun IrProperty.addDefaultGetterWithSameVisibility(parentClass: IrClass, builtIns: IrBuiltIns) {
    val field = backingField!!
    addGetter {
        origin = IrDeclarationOrigin.DEFAULT_PROPERTY_ACCESSOR
        returnType = field.type
        visibility = this@addDefaultGetterWithSameVisibility.visibility
    }.apply {
        parameters = listOf(parentClass.thisReceiver!!.copyTo(this))
        body = factory.createBlockBody(
            UNDEFINED_OFFSET, UNDEFINED_OFFSET, listOf(
                IrReturnImpl(
                    UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                    builtIns.nothingType,
                    symbol,
                    IrGetFieldImpl(
                        UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                        field.symbol,
                        field.type,
                        IrGetValueImpl(
                            UNDEFINED_OFFSET, UNDEFINED_OFFSET,
                            dispatchReceiverParameter!!.type,
                            dispatchReceiverParameter!!.symbol
                        )
                    )
                )
            )
        )
    }
}
