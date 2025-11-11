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
            implementation(libs.androidx.datastore.preferences)
            implementation(libs.okio.fakefilesystem)
        }
    }
}
