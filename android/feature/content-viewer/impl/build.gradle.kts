kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.contentviewer.impl") {
        routeBinding()
        screenshotTests()

        dependencies {
            implementation(project(":feature:content-viewer:api"))
            implementation(project(":feature:tldr-summary:api"))
            implementation(project(":core:launcher"))
            implementation(project(":core:ui:pattern"))
            implementation(project(":kmp:presentation:content-viewer"))
            implementation(libs.androidx.core)
            implementation(libs.androidx.activity.compose)
        }
    }
}
