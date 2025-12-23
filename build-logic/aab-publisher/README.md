A Gradle plugin for publishing the Android App Bundle for a single build variant to Google Play (Internal Testing track).

It uses [gradle-play-publisher](https://github.com/Triple-T/gradle-play-publisher)'s `android-publisher` library internally to interact with the [Android Publisher API](https://developers.google.com/android-publisher).

**The plugin is fully compatible with AGP 9's new DSL.**

## Usage

Apply the plugin in your app module's `build.gradle.kts` and configure the variant to publish and the service account credentials:

```kotlin
plugins {
    id("io.github.reactivecircus.aab-publisher")
}

aabPublisher {
    // The build variant to publish
    variant.set("prodRelease")
    // Path to the Google Play Service Account JSON credentials file
    serviceAccountCredentials.set(file("play-services-account.json"))
}
```

To publish the bundle to Google Play:

```bash
./gradlew publishBundleToGooglePlay
```

The task will upload the Android App Bundle (AAB) file for the configured build variant to Google Play's Internal Testing track.
