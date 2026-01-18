plugins {
    id("kstreamlined")
}

kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.settings.impl") {
        screenshotTests()

        dependencies {
            implementation(project(":feature:settings:api"))
            implementation(project(":feature:licenses:api"))
            implementation(project(":core:navigation"))
            implementation(project(":kmp:settings-datasource"))
            implementation(project(":kmp:presentation:settings"))
            implementation(project(":kmp:app-info"))
        }
    }
}
