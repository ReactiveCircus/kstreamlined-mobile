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
            api(project(":kmp:feed-model"))
            implementation(project(":kmp:capsule:runtime"))
            implementation(project(":kmp:feed-datasource"))

            testImplementation(project(":kmp:database-testing"))
            testImplementation(project(":kmp:capsule:testing"))
            testImplementation(libs.kotlinx.coroutines.test)
            testImplementation(libs.turbine)
        }
    }
}
