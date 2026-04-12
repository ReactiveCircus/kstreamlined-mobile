import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

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
            api(project(":kmp:remote:common"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kermit)

            testImplementation(libs.kotlinx.coroutines.test)
        }
    }
}
