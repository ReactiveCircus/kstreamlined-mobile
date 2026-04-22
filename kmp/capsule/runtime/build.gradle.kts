import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

kstreamlined {
    kmpLibrary {
        targets {
            jvm()
            ios()
        }
        compose()
        metro()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            // TODO consider changing to implementation once RecompositionMode is no longer a public constructor parameter
            api(libs.molecule.runtime)
            api(libs.androidx.compose.runtime.retain)
        }
    }
}
