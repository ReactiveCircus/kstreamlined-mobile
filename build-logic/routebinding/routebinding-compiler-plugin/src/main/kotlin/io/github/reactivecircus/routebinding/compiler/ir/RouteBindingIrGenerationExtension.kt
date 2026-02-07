package io.github.reactivecircus.routebinding.compiler.ir

import io.github.reactivecircus.routebinding.compiler.ClassIds
import io.github.reactivecircus.routebinding.compiler.ir.symbols.Nav3Symbols
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.util.classIdOrFail
import org.jetbrains.kotlin.ir.util.getPackageFragment
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name

internal class RouteBindingIrGenerationExtension(
    private val messageCollector: MessageCollector,
) : IrGenerationExtension {
    override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
        val routeBindingFunctions = moduleFragment.files.asSequence()
            .flatMap { it.declarations }
            .filterIsInstance<IrSimpleFunction>()
            .filter { it.hasAnnotation(ClassIds.RouteBinding.Annotation) }
        if (routeBindingFunctions.none()) return

        val nav3Symbols = Nav3Symbols.create(
            pluginContext,
            messageCollector,
            entryProviderScopeClassId = ClassIds.Nav3.EntryProviderScope,
        )
        if (nav3Symbols == null) return

        moduleFragment.transform(
            NavEntryInstallerClassTransformer(
                pluginContext,
                nav3Symbols,
                routeBindingFunctions,
            ),
            null,
        )

        val hintGenerator = MetroHintGenerator(pluginContext, moduleFragment)
        val navEntryInstallerClasses = findNavEntryInstallerClasses(moduleFragment, routeBindingFunctions)
        for (installerClass in navEntryInstallerClasses) {
            hintGenerator.generateHint(installerClass)
        }
    }

    private fun findNavEntryInstallerClasses(
        moduleFragment: IrModuleFragment,
        routeBindingFunctions: Sequence<IrSimpleFunction>,
    ): List<IrClass> {
        // TODO reuse NavEntryInstaller class ids computation in NavEntryInstallerClassTransformer
        val expectedClassIds = routeBindingFunctions.map { function ->
            val classNameSuffix = ClassIds.RouteBinding.NavEntryInstaller.shortClassName.asString()
            ClassId(
                function.getPackageFragment().packageFqName,
                Name.identifier("${function.name.asString()}_$classNameSuffix"),
            )
        }.toSet()

        return moduleFragment.files.asSequence()
            .flatMap { it.declarations }
            .filterIsInstance<IrClass>()
            .filter { it.classIdOrFail in expectedClassIds }
            .toList()
    }
}
