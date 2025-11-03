Chameleon
=========

A Kotlin compiler plugin that supports generating parameterized snapshot tests for each theme variant with [Burst](https://github.com/cashapp/burst).

## How It Works

When a test class is annotated with `@Chameleon`, the compiler plugin does IR transforms to:
- Add a `themeVariant: ThemeVariant` (enum) property to the class as a constructor parameter
- Assign the value of the `themeVariant` property to the `themeVariant` parameter of all `snapshot` function calls in the class

Before the IR transform: 

```kt
@Burst
@Chameleon
class SampleTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_MyUi() {
        snapshotTester.snapshot {
            MyUi()
        }
    }
}
```

After the transform the IR equivalent of the following will be produced:

```kt
@Burst
@Chameleon
class SampleTest(
    private val themeVariant: ThemeVariant // added during IR transform
) {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_MyUi() {
        snapshotTester.snapshot(
            themeVariant = themeVariant, // added during IR transform
        ) {
            MyUi()
        }
    }
}
```

Burst will then generate separate tests for each enum entry of `ThemeVariant`.

## Setup

Apply the Chameleon Gradle plugin in `build.gradle.kts`:

```kotlin
plugins {
    id("io.github.reactivecircus.chameleon")
    id("app.cash.burst")
}
```

Configure the `snapshotFunction` and `themeVariant` to use in the format where the packages are delimited by `/` and the class names are delimited by `.`:

```kotlin
chameleon {
    snapshotFunction.set("com/example/SnapshotTester.snapshot")
    themeVariantEnum.set("com/example/ThemeVariant")
}
```

The `SnapshotTester` and `ThemeVariant` need to be visible in the test source set of the module:

```kotlin
class SnapshotTester {
    fun snapshot(
        themeVariant: ThemeVariant = ThemeVariant.Light,
    ) {
        ...
    }
}

enum class ThemeVariant {
    Light,
    Dark,
}
```

## Usage

Annotate the snapshot test class with both `@Burst` and `@Chameleon`:

```kotlin
import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon

@Burst
@Chameleon
class ButtonTest {
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_Button() {
        snapshotTester.snapshot {
            Button(text = "Button", onClick = {})
        }
    }
}
```

When running the tests, Burst will generate separate tests for each enum entry of `ThemeVariant`:

```
ButtonTest_Dark > snapshot_Button PASSED
ButtonTest_Light > snapshot_Button PASSED
```

## IDE Support

To enable rendering diagnostics directly in the IDE:
1. Make sure K2 mode is enabled
2. Set `kotlin.k2.only.bundled.compiler.plugins.enabled` to false in the Registry
