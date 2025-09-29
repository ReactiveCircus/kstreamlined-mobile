package io.github.reactivecircus.chameleon.compiler

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
