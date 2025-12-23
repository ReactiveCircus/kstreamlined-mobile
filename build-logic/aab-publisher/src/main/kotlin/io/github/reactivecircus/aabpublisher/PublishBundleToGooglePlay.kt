package io.github.reactivecircus.aabpublisher

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault

@DisableCachingByDefault
internal abstract class PublishBundleToGooglePlay : DefaultTask() {

    @TaskAction
    fun execute() {
        println("~~~~~ TODO - publishing aab bundle to google play...")
    }
}
