import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined")
    id("kstreamlined.compose")
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
        api(project(":kmp:presentation:common"))
        implementation(project(":kmp:feed-datasource"))

        testImplementation(project(":kmp:remote:testing"))
        testImplementation(project(":kmp:database-testing"))
        testImplementation(libs.kotlinx.coroutines.test)
        testImplementation(libs.turbine)
    }
}
