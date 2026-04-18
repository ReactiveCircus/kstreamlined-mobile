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
            api(project(":kmp:presentation:common"))
            implementation(project(":kmp:arch:metro-retain"))
            implementation(project(":kmp:feed-datasource"))
            implementation(project(":kmp:feed-sync:common"))
            implementation(project(":kmp:pretty-time"))

            testImplementation(project(":kmp:feed-sync:testing"))
            testImplementation(project(":kmp:remote:testing"))
            testImplementation(project(":kmp:database-testing"))
            testImplementation(libs.kotlinx.coroutines.test)
            testImplementation(libs.turbine)
        }
    }
}
