RouteBinding
============

A Kotlin compiler plugin that generates Nav3 entry and Composable function bindings aggregated across modules using [Metro](https://github.com/ZacSweers/metro)'s multibinding.

## How It Works

The plugin generates a `NavEntryInstaller` class implementation for each `@Composable` function annotated with `@RouteBinding`.

`NavEntryInstaller` is an interface from RouteBinding's runtime library:

```kt
interface NavEntryInstaller {
    context(entryProviderScope: EntryProviderScope<NavKey>, sharedTransitionScope: SharedTransitionScope)
    fun install(backStack: NavBackStack<NavKey>)
}
```

Given the following annotated function in a feature module:

```kt
@RouteBinding(SettingsRoute::class)
@Composable
internal fun SharedTransitionScope.SettingsScreen(
    backStack: NavBackStack<NavKey>,
    route: SettingsRoute,
) { ... }
```

The compiler plugin generates the IR equivalent of:

```kt
@ContributesIntoSet(AppScope::class)
class SettingsScreen_NavEntryInstaller : NavEntryInstaller {
    context(entryProviderScope: EntryProviderScope<NavKey>, sharedTransitionScope: SharedTransitionScope)
    override fun install(backStack: NavBackStack<NavKey>) {
        entryProviderScope.entry<SettingsRoute> {
            sharedTransitionScope.SettingsScreen(
                backStack = backStack,
                route = it,
            )
        }
    }
}
```

Metro's `@ContributesIntoSet` automatically contributes each generated installer into a `Set<NavEntryInstaller>` across all modules in the dependency graph accessible in the app module.

The collected `NavEntryInstaller`s can then be used to `install` the binding using `NavDisplay`s `entryProvider` DSL.

## Setup

Apply the RouteBinding Gradle plugin in your module's `build.gradle.kts`:

```kt
plugins {
    id("io.github.reactivecircus.routebinding")
    id("dev.zacsweers.metro")
}
```

That's it.

## Usage

### 1. Contribute nav entries with `@RouteBinding`

In each feature module (or app module), annotate the screen-level `@Composable` function with `@RouteBinding`:

```kt
@RouteBinding(SettingsRoute::class)
@Composable
internal fun SharedTransitionScope.SettingsScreen(
    backStack: NavBackStack<NavKey>,
    route: SettingsRoute,
) { ... }
```

Supported optional parameter types:
- `SharedTransitionScope` — as receiver or context parameter
- `NavBackStack<NavKey>` — the navigation back stack
- A subtype of `NavKey` — the route for this screen (must match the `val route: KClass<out NavKey>` type in the `@RouteBinding` declaration)
- Any parameter with a default value is also allowed

### 2. Collect installers via Metro

In the app module, expose the contributed set of `NavEntryInstaller` from the dependency graph:

```kt
@DependencyGraph(AppScope::class)
interface AppGraph {
    val navEntryInstallers: Set<NavEntryInstaller>
}
```

### 3. Install entries with Nav3

Use the collected installers to wire up the navigation graph using Nav3's `entryProvider`:

```kt
val backStack = rememberNavBackStack(MainRoute)

SharedTransitionLayout {
    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            appGraph.navEntryInstallers.forEach {
                it.install(backStack)
            }
        },
    )
}
```

## IDE Support

To enable rendering diagnostics directly in the IDE:
1. Make sure K2 mode is enabled
2. Set `kotlin.k2.only.bundled.compiler.plugins.enabled` to false in the Registry
