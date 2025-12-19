package io.github.reactivecircus.v2p.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.NormalizeLineEndings
import java.io.Serializable

@CacheableTask
internal abstract class GeneratePainterAccessors : DefaultTask() {
    @get:Input
    abstract val packageName: Property<String>

    @get:InputFiles
    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:NormalizeLineEndings
    abstract val resourceDirectories: ConfigurableFileCollection

    @get:Input
    abstract val codegenOptionsMap: MapProperty<String, PerGroupCodegenConfigs>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    init {
        group = "V2P"
        description = "Generates type-safe Compose `Painter` accessors from vector drawables."
    }

    @TaskAction
    fun execute() {
        val drawableFileNames = resourceDirectories.asFileTree
            .matching { it.include("**/drawable/*.xml") }.files
            .map { it.name }
            .sorted()

        codegenOptionsMap.get().forEach { (containerName, configs) ->
            PainterAccessorsGenerator.buildFileSpec(
                packageName = packageName.get(),
                containerName = containerName,
                configs = configs,
                drawableFileNames = drawableFileNames.filter { it.startsWith(configs.prefix) },
            ).writeTo(outputDir.get().asFile)
        }
    }
}

internal class PerGroupCodegenConfigs(
    val prefix: String,
    val generateAsListFunction: Boolean,
    val subpackage: String?,
) : Serializable {
    companion object {
        @Suppress("unused")
        private const val serialVersionUID: Long = 1L
    }
}
