plugins {
    id("kstreamlined")
}

kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.savedforlater") {
        screenshotTests()

        dependencies {
            implementation(project(":feature:settings:api"))
            implementation(project(":core:ui:feed"))
            implementation(project(":core:ui:pattern"))
            implementation(project(":kmp:feed-datasource"))
            implementation(project(":kmp:presentation:saved-for-later"))
        }
    }
}
