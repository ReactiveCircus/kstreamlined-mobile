plugins {
    id("kstreamlined")
}

kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.home") {
        screenshotTests()

        dependencies {
            implementation(project(":core:ui:feed"))
            implementation(project(":core:ui:pattern"))
            implementation(project(":kmp:feed-datasource"))
            implementation(project(":kmp:feed-sync:common"))
            implementation(project(":kmp:presentation:home"))
            implementation(libs.androidx.activity.compose)
        }
    }
}
