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
            implementation(project(":kmp:settings-datasource"))
            implementation(project(":kmp:app-info"))

            testImplementation(project(":kmp:datastore-testing"))
            testImplementation(project(":kmp:capsule:testing"))
            testImplementation(libs.kotlinx.coroutines.test)
            testImplementation(libs.turbine)
        }
    }
}
