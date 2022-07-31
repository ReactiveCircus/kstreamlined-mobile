plugins {
    id("kstreamlined.kmm.jvm-and-ios")
    id("kstreamlined.kmm.test")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":kmm:data-common"))
                implementation(libs.apollo.runtime)
                implementation(libs.apollo.normalizedCache)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kermit)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.apollo.testingSupport)
            }
        }
    }
}
