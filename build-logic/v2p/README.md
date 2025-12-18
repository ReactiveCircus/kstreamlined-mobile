V2P
===

A Gradle plugin that generates type-safe Compose `Painter` accessors from vector drawables.

```diff
-Icon(painterResource(R.drawable.ic_kotlin), contentDescription = null)
+Icon(MyIcons.Kotlin, contentDescription = null)
```

## Usage

Apply the V2P Gradle plugin in Android Library module's `build.gradle.kts`:

```kt
plugins {
    id("io.github.reactivecircus.v2p")
}
```

TODO

```kt
v2p {
    generate("KSIcons") {
        prefix.set("ic_")
        generateAsListFunction.set(true)
    }
    runCodegenOnSync(variantName = "devDebug")
}
```

TODO

```kt
v2p {
    generate("KSIcons") {
        prefix.set("ic_")
        generateAsListFunction.set(true)
    }
    generate("KSGraphics") {
        prefix.set("graphics_")
    }
}
```

TODO
