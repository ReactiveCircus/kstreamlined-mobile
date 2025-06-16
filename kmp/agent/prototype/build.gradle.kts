plugins {
    id("kstreamlined.kmp.jvm-and-ios")
}

kotlin {
    sourceSets {
        jvmMain {
            dependencies {
                implementation(libs.koog.agents)
            }
        }
    }
}
