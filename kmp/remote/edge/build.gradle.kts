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
        unitTests()
    }
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        api(project(":kmp:remote:common"))
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kermit)

        testImplementation(libs.kotlinx.coroutines.test)
    }
}
