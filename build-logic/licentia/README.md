Licentia
========

A Gradle plugin that generates Kotlin source from [Licensee](https://github.com/cashapp/licensee)'s Json report.

## How It Works

[Licensee](https://github.com/cashapp/licensee) generates `artifacts.json` file for license validation.

It provides a `bundleAndroidAsset` option to bundle the `artifacts.json` file as an asset in the APK, though consuming it requires parsing at runtime.

This plugin parses the `artifacts.json` file at compile time to generate Kotlin code that can be consumed directly to render licenses info in the app.

## Usage

Apply the Licentia Gradle plugin in app module's `build.gradle.kts`:

```kt
plugins {
    id("io.github.reactivecircus.licentia")
    id("app.cash.licensee")
}
```

The Gradle plugin automatically adds the `io.github.reactivecircus.licentia:licentia-runtime` as an `implementation` dependency for the project.

The runtime library provides an `Artifact` model representing each artifact in the Json report, and a `LicensesInfo` interface that can be injected and consumed by library modules:

```kt
interface LicensesInfo {
    val artifacts: List<Artifact>
}
```

The plugin generates an `AllLicensesInfo` object that implements the `LicensesInfo` interface in the app module.

`AllLicensesInfo.artifacts: List<LicensesInfo.Artifact>` will include all artifacts in Licensee's Json report.

You can also bind `AllLicensesInfo` to the `LicensesInfo` interface in the DI graph from the app module so library modules can inject and consume it:

```kt
@Provides
@Singleton
fun licensesInfo(): LicensesInfo = AllLicensesInfo
```

The codegen task runs automatically when building the app module. To run it manually:

```
./gradlew generateLicensesInfoSource<Variant>
```

where `<Variant>` is the name of the build variant.

The codegen task can also be configured to automatically run on Gradle sync for a particular build variant:

```kt
licentia {
    runCodegenOnSync(variantName = "devDebug")
}
```

To access the `LicensesInfo` APIs from library modules, add a dependency on the runtime library directly:

```kt
dependencies {
    implementation("io.github.reactivecircus.licentia:licentia-runtime")
}
```
