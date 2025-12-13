import dev.detekt.gradle.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `java-gradle-plugin`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.lint)
    alias(libs.plugins.detekt)
}

group = project.property("GROUP") as String

gradlePlugin {
    plugins {
        register("licentia") {
            id = "io.github.reactivecircus.licentia"
            implementationClass = "io.github.reactivecircus.licentia.gradle.LicentiaGradlePlugin"
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
    explicitApi()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

detekt {
    source.setFrom(file("src/"))
    config.setFrom(file("$rootDir/../detekt.yml"))
    buildUponDefaultConfig = true
    parallel = true
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = JvmTarget.JVM_17.target
    reports {
        checkstyle.required.set(false)
        sarif.required.set(false)
        markdown.required.set(false)
    }
}

dependencies {
    // enable Ktlint formatting
    detektPlugins(libs.plugin.detektKtlintWrapper)

    compileOnly(libs.plugin.agp)
    compileOnly(libs.plugin.licensee)

    testImplementation(libs.kotlin.test.junit)
}
