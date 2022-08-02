plugins {
    id("kstreamlined.kmm.jvm-and-ios")
    id("kstreamlined.kmm.test")
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":kmm:data-common"))
                api(libs.apollo.runtime)
                api(libs.apollo.normalizedCache)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kermit)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(project(":kmm:test-utils"))
                implementation(libs.apollo.testingSupport)
            }
        }
    }
}
