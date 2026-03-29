// ENABLE_COMPOSE_COMPILER

// FILE: file1.kt

package screen1

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.reactivecircus.routebinding.runtime.RouteBinding

@RouteBinding(DummyRoute::class)
@Composable
fun SharedTransitionScope.DummyScreen(
    backStack: NavBackStack<NavKey>,
    route: DummyRoute,
) {
}

data object DummyRoute : NavKey

// FILE: file2.kt

package screen2

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.reactivecircus.routebinding.runtime.RouteBinding

@RouteBinding(DummyRoute::class)
@Composable
fun SharedTransitionScope.DummyScreen(
    backStack: NavBackStack<NavKey>,
    route: DummyRoute,
) {
}

data object DummyRoute : NavKey

// FILE: main.kt

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.createGraph
import dev.zacsweers.metro.DependencyGraph
import io.github.reactivecircus.routebinding.runtime.NavEntryInstaller
import kotlin.test.assertEquals

@DependencyGraph(AppScope::class)
interface AppGraph {
    val installers: Set<NavEntryInstaller>
}

fun box(): String {
    val graph = createGraph<AppGraph>()
    assertEquals(
        listOf(
            "screen1.DummyScreen_NavEntryInstaller",
            "screen2.DummyScreen_NavEntryInstaller",
        ),
        graph.installers.mapNotNull { it::class.qualifiedName }.sorted(),
    )
    return "OK"
}
