import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

kstreamlined {
    kmpLibrary {
        targets {
            jvm()
            ios()
        }
        compose()
        metro()
        unitTests()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            implementation(project(":kmp:capsule:runtime"))
            implementation(project(":kmp:feed-datasource"))

            testImplementation(project(":kmp:remote:testing"))
            testImplementation(project(":kmp:database-testing"))
            testImplementation(project(":kmp:capsule:testing"))
            testImplementation(libs.kotlinx.coroutines.test)
            testImplementation(libs.turbine)
        }
    }
}
