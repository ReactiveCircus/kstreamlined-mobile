package io.github.reactivecircus.licentia.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.NormalizeLineEndings

@CacheableTask
internal abstract class GenerateLicensesInfoSource : DefaultTask() {
    @get:Input
    abstract val packageName: Property<String>

    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:NormalizeLineEndings
    abstract val artifactsJsonFile: RegularFileProperty

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    init {
        group = "Licentia"
        description = "Generates `LicencesInfo` implementation from Licensee plugin's Json report."
    }

    @TaskAction
    fun execute() {
        LicensesInfoGenerator.buildFileSpec(
            packageName = packageName.get(),
            artifactsJson = artifactsJsonFile.get().asFile.readText(),
        ).writeTo(outputDir.get().asFile)
    }
}
