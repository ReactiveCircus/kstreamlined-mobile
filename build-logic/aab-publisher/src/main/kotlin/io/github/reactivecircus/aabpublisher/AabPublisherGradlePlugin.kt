package io.github.reactivecircus.aabpublisher

import com.android.build.api.artifact.SingleArtifact
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class AabPublisherGradlePlugin : Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        val aabPublisherExtension = target.extensions.create("aabPublisher", AabPublisherExtension::class.java)
        var androidAppPluginApplied = false
        pluginManager.withPlugin("com.android.application") {
            androidAppPluginApplied = true
            extensions.configure(ApplicationAndroidComponentsExtension::class.java) { extension ->
                extension.onVariants { variant ->
                    if (!variant.name.equals(aabPublisherExtension.variant.get(), ignoreCase = true)) return@onVariants
                    tasks.register(
                        "publishBundleToGooglePlay",
                        PublishBundleToGooglePlay::class.java,
                    ) {
                        it.group = "AAB Publisher"
                        it.description = getTaskDescription(variant.name)
                        it.bundle.set(variant.artifacts.get(SingleArtifact.BUNDLE))
                    }
                }
            }
        }
        afterEvaluate {
            check(androidAppPluginApplied) {
                "AAB Publisher requires the `com.android.application` plugin to be applied to the same project," +
                    " it's missing in $displayName."
            }
        }
    }

    private fun getTaskDescription(variantName: String): String =
        "Publishes the Android App Bundle to Google Play for the $variantName variant."
}
