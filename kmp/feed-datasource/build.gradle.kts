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
        implementation(project(":kmp:remote:common"))
        implementation(project(":kmp:database"))
        api(project(":kmp:feed-model"))
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kermit)

        testImplementation(project(":kmp:remote:testing"))
        testImplementation(project(":kmp:database-testing"))
        testImplementation(libs.kotlinx.coroutines.test)
        testImplementation(libs.turbine)
    }
}
