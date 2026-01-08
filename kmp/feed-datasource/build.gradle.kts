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
        metro()
        unitTests()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            api(project(":kmp:feed-model"))
            implementation(project(":kmp:remote:common"))
            implementation(project(":kmp:database"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kermit)

            testImplementation(project(":kmp:remote:testing"))
            testImplementation(project(":kmp:database-testing"))
            testImplementation(libs.kotlinx.coroutines.test)
            testImplementation(libs.turbine)
        }
    }
}
