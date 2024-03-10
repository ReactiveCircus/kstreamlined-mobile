plugins {
    id("kstreamlined.kmp.common")
    id("kstreamlined.kmp.test")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":kmp:presentation:common"))
                implementation(project(":kmp:feed-datasource"))
                implementation(project(":kmp:feed-sync"))
                implementation(project(":kmp:pretty-time"))
            }
        }
    }
}
