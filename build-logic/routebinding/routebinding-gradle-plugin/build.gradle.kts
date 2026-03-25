import dev.detekt.gradle.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `java-gradle-plugin`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.lint)
    alias(libs.plugins.detekt)
    alias(libs.plugins.testkit)
}

group = project.property("GROUP") as String

gradlePlugin {
    plugins {
        register("routeBinding") {
            id = "io.github.reactivecircus.routebinding"
            implementationClass = "io.github.reactivecircus.routebinding.gradle.RouteBindingGradlePlugin"
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
    explicitApi()
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

detekt {
    source.setFrom(file("src/"))
    config.setFrom(file("$rootDir/../detekt.yml"))
    buildUponDefaultConfig = true
    parallel = true
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = JvmTarget.JVM_21.target
    reports {
        checkstyle.required.set(false)
        sarif.required.set(false)
        markdown.required.set(false)
    }
}

tasks.withType<Test> {
    systemProperty("io.github.reactivecircus.routebinding.gradle.test.kotlin-version", libs.versions.kotlin.get())
    systemProperty("io.github.reactivecircus.routebinding.gradle.test.metro-version", libs.versions.metro.get())
}

gradleTestKitSupport {
    withSupportLibrary()
}

dependencies {
    // enable Ktlint formatting
    detektPlugins(libs.plugin.detektKtlintWrapper)

    compileOnly(libs.plugin.kotlin)

    functionalTestImplementation(libs.kotlin.test.junit)
}
