kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.kotlinyoutubevideo.impl") {
        routeBinding()
        screenshotTests()

        dependencies {
            implementation(project(":feature:kotlin-youtube-video:api"))
            implementation(project(":core:launcher"))
            implementation(project(":core:ui:pattern"))
            implementation(project(":core:ui:util"))
            implementation(project(":core:web"))
            implementation(project(":core:display"))
            implementation(project(":kmp:presentation:kotlin-youtube-video"))
            implementation(libs.androidx.activity.compose)
        }
    }
}
