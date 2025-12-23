package io.github.reactivecircus.aabpublisher

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import org.gradle.work.NormalizeLineEndings

@DisableCachingByDefault
internal abstract class PublishBundleToGooglePlay : DefaultTask() {
    @get:InputFile
    @get:PathSensitive(PathSensitivity.RELATIVE)
    @get:NormalizeLineEndings
    abstract val bundle: RegularFileProperty

    @TaskAction
    fun execute() {
        println("Publishing ${bundle.asFile.get()} to google play...")
        // TODO
    }
}
