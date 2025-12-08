plugins {
    id("kstreamlined")
}

kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.settings") {
        screenshotTests()

        dependencies {
            implementation(project(":kmp:settings-datasource"))
            implementation(project(":kmp:presentation:settings"))
            implementation(project(":kmp:app-info"))
        }
    }
}
