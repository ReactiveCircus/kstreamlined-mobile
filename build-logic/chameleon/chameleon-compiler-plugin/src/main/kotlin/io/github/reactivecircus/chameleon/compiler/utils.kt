package io.github.reactivecircus.chameleon.compiler

import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName

internal fun String.toClassId(): ClassId =
    FqName(this).run { ClassId(parent(), shortName()) }

internal fun String.toCallableId(): CallableId =
    FqName(this).run { CallableId(parent(), shortName()) }
