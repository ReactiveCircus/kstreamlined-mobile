import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined")
}

kstreamlined {
    kmpLibrary {
        targets {
            jvm()
            ios()
        }

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            // TODO check if stability annotations are needed
            implementation(libs.androidx.compose.runtimeAnnotation)
        }
    }
}
