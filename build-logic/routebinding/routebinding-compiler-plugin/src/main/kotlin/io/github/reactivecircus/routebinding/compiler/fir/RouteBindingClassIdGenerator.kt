package io.github.reactivecircus.routebinding.compiler.fir

import io.github.reactivecircus.routebinding.compiler.ClassIds
import org.jetbrains.kotlin.fir.symbols.impl.FirNamedFunctionSymbol
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name

internal object RouteBindingClassIdGenerator {
    /**
     * Generates [ClassId] of the NavEntryInstaller class to be generated for the given source function annotated with `@Routebinding`.
     */
    fun generateNavInstallerClassId(sourceFunction: FirNamedFunctionSymbol): ClassId {
        val packageFqName = sourceFunction.callableId.packageName
        val classNameSuffix = ClassIds.RouteBinding.NavEntryInstaller.shortClassName.asString()
        val className = Name.identifier("${sourceFunction.name.asString()}_$classNameSuffix")
        return ClassId(packageFqName, className)
    }
}
