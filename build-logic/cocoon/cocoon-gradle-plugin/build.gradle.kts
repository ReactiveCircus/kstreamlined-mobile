import io.gitlab.arturbosch.detekt.Detekt
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    alias(libs.plugins.detekt)
}

gradlePlugin {
    plugins {
        register("cocoon") {
            id = "io.github.reactivecircus.cocoon"
            implementationClass = "io.github.reactivecircus.cocoon.gradle.CocoonPlugin"
        }
    }
}

kotlin {
    explicitApi()
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = JavaVersion.VERSION_11.toString()
    targetCompatibility = JavaVersion.VERSION_11.toString()
}

detekt {
    source.from(files("src/"))
    config.from(files("$rootDir/../detekt.yml"))
    buildUponDefaultConfig = true
    allRules = true
    parallel = true
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = JvmTarget.JVM_11.target
    reports {
        xml.required.set(false)
        txt.required.set(false)
        sarif.required.set(false)
        md.required.set(false)
    }
}

dependencies {
    // enable Ktlint formatting
    add("detektPlugins", libs.plugin.detektFormatting)

    compileOnly(libs.plugin.kotlin)
}
