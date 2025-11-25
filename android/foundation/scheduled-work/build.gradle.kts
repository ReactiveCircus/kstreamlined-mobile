import com.android.build.api.variant.HasUnitTestBuilder

plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.ksp")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.foundation.scheduledwork"
}

androidComponents {
    beforeVariants {
        (it as HasUnitTestBuilder).enableUnitTest = true
    }
}

dependencies {
    implementation(project(":kmp:feed-sync:common"))
    implementation(project(":kmp:settings-datasource"))

    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.hilt.work)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.androidx.tracing)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    testImplementation(project(":kmp:datastore-testing"))
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
}
