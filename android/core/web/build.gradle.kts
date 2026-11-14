kstreamlined {
    androidCoreLibrary("io.github.reactivecircus.kstreamlined.android.core.web") {
        compose()

        dependencies {
            implementation(project(":core:display"))
            implementation(libs.androidx.compose.foundation)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.webkit)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kermit)
        }
    }
}
