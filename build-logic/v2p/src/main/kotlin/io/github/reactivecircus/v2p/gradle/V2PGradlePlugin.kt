package io.github.reactivecircus.v2p.gradle

import com.android.build.api.variant.LibraryAndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.extensions.stdlib.capitalized

public class V2PGradlePlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        val v2pExtension = target.extensions.create(
            V2PExtension::class.java,
            "v2p",
            V2PExtensionImpl::class.java,
        ) as V2PExtensionImpl
        var androidLibraryPluginApplied = false
        pluginManager.withPlugin("com.android.library") {
            androidLibraryPluginApplied = true
            extensions.configure(LibraryAndroidComponentsExtension::class.java) { extension ->
                extension.onVariants { variant ->
                    val kotlinSources = variant.sources.kotlin ?: return@onVariants
                    val resSources = variant.sources.res ?: return@onVariants
                    val generatePainterAccessors = tasks.register(
                        "generateComposePainterAccessors${variant.name.capitalized()}",
                        GeneratePainterAccessors::class.java,
                    ) {
                        it.packageName.set(variant.namespace)
                        it.resourceDirectories.setFrom(resSources.static)
                        it.codegenOptionsMap.set(
                            v2pExtension.groupsOfCodegenOptions.mapValues { (_, options) ->
                                PerGroupCodegenConfigs(
                                    prefix = options.prefix.get(),
                                    generateAsListFunction = options.generateAsListFunction.get(),
                                    subpackage = options.subpackage.orNull,
                                )
                            },
                        )
                        it.outputDir.set(layout.buildDirectory.dir("generated/source/v2p/${variant.name}"))
                    }
                    kotlinSources.addGeneratedSourceDirectory(
                        generatePainterAccessors,
                        GeneratePainterAccessors::outputDir,
                    )
                    // automatically run the GeneratePainterAccessors task
                    // for the specified variant on Gradle Sync
                    if (variant.name == v2pExtension.variantForCodegenOnSync) {
                        target.tasks.maybeCreate("prepareKotlinIdeaImport")
                            .dependsOn(generatePainterAccessors)
                    }
                }
            }
        }
        afterEvaluate {
            check(androidLibraryPluginApplied) {
                "V2P requires the `com.android.library` plugin to be applied to the same project," +
                    " it's missing in $displayName."
            }
        }
    }
}
