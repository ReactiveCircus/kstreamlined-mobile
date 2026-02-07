package io.github.reactivecircus.routebinding.compiler.ir

import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.descriptors.impl.EmptyPackageFragmentDescriptor
import org.jetbrains.kotlin.fir.backend.FirMetadataSource
import org.jetbrains.kotlin.fir.builder.buildPackageDirective
import org.jetbrains.kotlin.fir.declarations.FirDeclarationOrigin
import org.jetbrains.kotlin.fir.declarations.builder.buildFile
import org.jetbrains.kotlin.ir.builders.declarations.buildFun
import org.jetbrains.kotlin.ir.builders.declarations.buildValueParameter
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.IrParameterKind
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.impl.IrFileImpl
import org.jetbrains.kotlin.ir.util.NaiveSourceBasedFileEntryImpl
import org.jetbrains.kotlin.ir.util.addChild
import org.jetbrains.kotlin.ir.util.addFile
import org.jetbrains.kotlin.ir.util.classIdOrFail
import org.jetbrains.kotlin.ir.util.defaultType
import org.jetbrains.kotlin.ir.util.fileEntry
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import kotlin.io.path.Path
import kotlin.io.path.absolutePathString

/**
 * Generates Metro-style contribution hint functions in IR for NavEntryInstaller contributions.
 *
 * This mirrors Metro's [HintGenerator] approach: generating hint functions in IR phase rather than FIR.
 * This avoids conflicts that occur when multiple FIR generators produce functions for the same CallableId.
 *
 * The generated hints are registered as metadata-visible so they can be discovered by downstream
 * compilations (i.e., the app module's Metro graph).
 */
internal class MetroHintGenerator(
    private val pluginContext: IrPluginContext,
    private val moduleFragment: IrModuleFragment,
) {
    private val metroHintsPackage = FqName("metro.hints")
    private val appScopeHintName = Name.identifier("AppScope")

    fun generateHint(navEntryInstallerClass: IrClass): IrSimpleFunction {
        val function = pluginContext.irFactory.buildFun {
            name = appScopeHintName
            origin = RouteBindingOrigins.ContributionHint
            returnType = pluginContext.irBuiltIns.unitType
            visibility = navEntryInstallerClass.visibility
        }.apply {
            parameters += buildValueParameter(this) {
                name = Name.identifier("contributed")
                type = navEntryInstallerClass.defaultType
                kind = IrParameterKind.Regular
            }
            // Create empty body - this function is never actually called
            body = DeclarationIrBuilder(pluginContext, symbol).irBlockBody { }
        }

        val fileName = hintFileName(navEntryInstallerClass)

        val firFile = buildFile {
            val metadataSource = navEntryInstallerClass.metadata as? FirMetadataSource.Class
            checkNotNull(metadataSource) {
                "Class ${navEntryInstallerClass.classIdOrFail} does not have a valid metadata source"
            }
            moduleData = metadataSource.fir.moduleData
            origin = FirDeclarationOrigin.Synthetic.PluginFile
            packageDirective = buildPackageDirective { packageFqName = metroHintsPackage }
            name = fileName
        }

        // Create a synthetic file path for incremental compilation support
        val fakeNewPath = Path(navEntryInstallerClass.fileEntry.name).parent.resolve(fileName)
        val hintFile = IrFileImpl(
            fileEntry = NaiveSourceBasedFileEntryImpl(fakeNewPath.absolutePathString()),
            packageFragmentDescriptor = EmptyPackageFragmentDescriptor(
                moduleFragment.descriptor,
                metroHintsPackage,
            ),
            module = moduleFragment,
        ).also {
            it.metadata = FirMetadataSource.File(firFile)
        }

        moduleFragment.addFile(hintFile)
        hintFile.addChild(function)
        pluginContext.metadataDeclarationRegistrar.registerFunctionAsMetadataVisible(function)

        return function
    }

    private fun hintFileName(navEntryInstallerClass: IrClass): String {
        val classId = navEntryInstallerClass.classIdOrFail
        val parts = buildList {
            addAll(classId.packageFqName.pathSegments().map(Name::asString))
            add(classId.shortClassName.asString())
            add(appScopeHintName.asString())
        }
        val fileNameWithoutExtension = parts.joinToString(separator = "") {
            it.replaceFirstChar { c -> if (c.isLowerCase()) c.titlecase() else c.toString() }
        }.replaceFirstChar { c ->
            if (c.isUpperCase()) c.lowercase() else c.toString()
        }
        return "$fileNameWithoutExtension.kt"
    }
}







