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
internal abstract class GenerateComposePainterAccessors : DefaultTask() {
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
        val drawableDir = resourceDirectories.files
            .flatMap { it.listFiles().orEmpty().toList() }
            .single { it.name == "drawable" }
        val drawableFiles = drawableDir.listFiles().orEmpty()
            .filter { it.name.endsWith(".xml") }

        println("-----------------")
        codegenOptionsMap.get().forEach { (objectName, configs) ->
            println("$objectName codegen configs:")
            println("--prefix: ${configs.prefix}")
            println("--generateAsListFunction: ${configs.generateAsListFunction}")
            println("--files to process:")
            drawableFiles.filter { it.name.startsWith(configs.prefix) }.forEach {
                println("----${it.name}")
            }
        }
    }
}

internal class PerGroupCodegenConfigs(
    val prefix: String,
    val generateAsListFunction: Boolean,
) : Serializable {
    companion object {
        @Suppress("unused")
        private const val serialVersionUID: Long = 1L
    }
}
