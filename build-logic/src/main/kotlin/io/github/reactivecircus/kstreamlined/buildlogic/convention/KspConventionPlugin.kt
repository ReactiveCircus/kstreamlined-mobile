package io.github.reactivecircus.kstreamlined.buildlogic.convention

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

internal class KspConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")

            extensions.findByType<LibraryAndroidComponentsExtension>()?.beforeVariants { variant ->
                extensions.findByType<KotlinAndroidProjectExtension>()
                    ?.sourceSets?.findByName(variant.name)?.kotlin?.srcDir("build/generated/ksp/${variant.name}/kotlin")
            }

            extensions.findByType<ApplicationAndroidComponentsExtension>()?.beforeVariants { variant ->
                extensions.findByType<KotlinAndroidProjectExtension>()
                    ?.sourceSets?.findByName(variant.name)?.kotlin?.srcDir("build/generated/ksp/${variant.name}/kotlin")
            }
        }
    }
}
