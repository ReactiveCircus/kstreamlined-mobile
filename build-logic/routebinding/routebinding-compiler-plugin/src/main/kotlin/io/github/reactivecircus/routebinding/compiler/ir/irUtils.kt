package io.github.reactivecircus.routebinding.compiler.ir

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.UNDEFINED_OFFSET
import org.jetbrains.kotlin.ir.builders.declarations.addConstructor
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irDelegatingConstructorCall
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrConstructor
import org.jetbrains.kotlin.ir.expressions.IrBody
import org.jetbrains.kotlin.ir.expressions.impl.IrInstanceInitializerCallImpl
import org.jetbrains.kotlin.ir.util.primaryConstructor
import org.jetbrains.kotlin.ir.util.superClass

internal fun IrClass.createDefaultConstructor(context: IrPluginContext): IrConstructor {
    return addConstructor {
        isPrimary = true
    }.apply {
        body = createDefaultConstructorBody(context)
    }
}

@Suppress("ReturnCount")
private fun IrConstructor.createDefaultConstructorBody(context: IrPluginContext): IrBody? {
    val parentClass = parent as? IrClass ?: return null
    val superClassConstructor = parentClass.superClass?.primaryConstructor
        ?: context.irBuiltIns.anyClass.owner.primaryConstructor
        ?: return null
    return DeclarationIrBuilder(context, symbol).irBlockBody {
        +irDelegatingConstructorCall(superClassConstructor)
        +IrInstanceInitializerCallImpl(
            startOffset = UNDEFINED_OFFSET,
            endOffset = UNDEFINED_OFFSET,
            classSymbol = parentClass.symbol,
            type = context.irBuiltIns.unitType,
        )
    }
}
