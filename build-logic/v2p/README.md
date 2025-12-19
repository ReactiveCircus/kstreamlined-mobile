V2P (Vector to Painter)
=======================

A Gradle plugin that generates type-safe Compose `Painter` accessors from vector drawables.

## How It Works

The plugin scans the `drawable` resource directory for XML vector drawables matching the configured prefix, and generates a Kotlin object with `Painter` accessors for each drawable.

Given the following vector drawables:

```
res/
├─ drawable/
│  ├─ ic_kotlin.xml
│  ├─ ic_mobile.xml
│  ├─ ic_settings.xml
```

The plugin can generate type-safe APIs for these drawables:

```kt
object KSIcons {
    val Kotlin: Painter @Composable get() = painterResource(R.drawable.ic_kotlin)
    val Mobile: Painter @Composable get() = painterResource(R.drawable.ic_mobile)
    val Settings: Painter @Composable get() = painterResource(R.drawable.ic_settings)
}
```

So `painterResource(R.drawable.ic_kotlin)` can be replaced with `KSIcons.Kotlin`:

```diff
-Icon(painterResource(R.drawable.ic_kotlin), contentDescription = null)
+Icon(KSIcons.Kotlin, contentDescription = null)
```

## Usage

Apply the V2P Gradle plugin in Android Library module's `build.gradle.kts`:

```kt
plugins {
    id("io.github.reactivecircus.v2p")
}
```

Configure name of the container object to generate and the prefix of the drawable files to include:

```kt
v2p {
    generate("KSIcons") {
        prefix.set("ic_")
    }
}
```

This will generate a `KSIcons` object with Compose `Painter` accessors for all vector drawables starting with `ic_`.

You can also generate multiple containers for different drawable prefixes:

```kt
v2p {
    generate("KSIcons") {
        prefix.set("ic_")
    }
    generate("KSGraphics") {
        prefix.set("graphics_")
    }
}
```

By default the container objects are generated in the root package of the module which is the configured Android `namespace`.

It's possible to move the generated code to a subpackage:

```kt
v2p {
    generate("KSIcons") {
        prefix.set("ic_")
        subpackage.set("ds.icon")
    }
}
```

Sometimes it's useful to be able to process all generated painters as a list.

This can be done by setting `generateAsListFunction` to `true`:


```kt
v2p {
    generate("KSIcons") {
        prefix.set("ic_")
        generateAsListFunction.set(true)
    }
}
```

This will generate a Composable `asList()` function with the container object:

```kt
object KSIcons {
    val Kotlin: Painter @Composable get() = painterResource(R.drawable.ic_kotlin)
    val Mobile: Painter @Composable get() = painterResource(R.drawable.ic_mobile)
    val Settings: Painter @Composable get() = painterResource(R.drawable.ic_settings)

    @Composable
    fun asList(): List<Painter> = listOf(Kotlin, Mobile, Settings)
}
```

### Running codegen

The codegen task runs automatically when building the module. To run it manually:

```
./gradlew generateComposePainterAccessors<Variant>
```

where `<Variant>` is the name of the build variant (e.g., `debug`, `release`).

The codegen task can also be configured to automatically run on Gradle sync for a particular build variant:

```kt
v2p {
    generate("KSIcons") {
        prefix.set("ic_")
    }
    runCodegenOnSync("debug")
}
```
