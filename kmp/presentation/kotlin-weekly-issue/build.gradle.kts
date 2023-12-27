plugins {
    id("kstreamlined.kmp.common")
    id("kstreamlined.kmp.test")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":kmp:presentation:common"))
                implementation(project(":kmp:data"))
                implementation(project(":kmp:core-utils"))
                api(project(":kmp:model"))
            }
        }
    }
}
