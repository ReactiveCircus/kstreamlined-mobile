kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl") {
        routeBinding()
        screenshotTests()

        dependencies {
            implementation(project(":core:ui:pattern"))
            implementation(project(":feature:tldr-summary:api"))
        }
    }
}
