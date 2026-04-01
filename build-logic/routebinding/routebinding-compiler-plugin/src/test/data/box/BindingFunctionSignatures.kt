// ENABLE_COMPOSE_COMPILER
// LANGUAGE: +ContextParameters

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.createGraph
import dev.zacsweers.metro.DependencyGraph
import io.github.reactivecircus.routebinding.runtime.NavEntryInstaller
import io.github.reactivecircus.routebinding.runtime.RouteBinding
import kotlin.test.assertEquals

// standard
@RouteBinding(DummyRoute::class)
@Composable
fun SharedTransitionScope.Screen1(
    backStack: NavBackStack<NavKey>,
    route: DummyRoute,
) {
}

// no receiver or params
@RouteBinding(DummyRoute::class)
@Composable
fun Screen2() {
}

// backstack only
@RouteBinding(DummyRoute::class)
@Composable
fun Screen3(
    backStack: NavBackStack<NavKey>,
) {
}

// route only
@RouteBinding(DummyRoute::class)
@Composable
fun Screen4(
    route: DummyRoute,
) {
}

// SharedTransitionScope as value param
@RouteBinding(DummyRoute::class)
@Composable
fun Screen5(
    sharedTransitionScope: SharedTransitionScope,
    backStack: NavBackStack<NavKey>,
    route: DummyRoute,
) {
}

// SharedTransitionScope as context parameter
@RouteBinding(DummyRoute::class)
@Composable
context(sharedTransitionScope: SharedTransitionScope)
fun Screen6(
    backStack: NavBackStack<NavKey>,
    route: DummyRoute,
) {
}

// all params as context parameters
@RouteBinding(DummyRoute::class)
@Composable
context(sharedTransitionScope: SharedTransitionScope, backStack: NavBackStack<NavKey>, route: DummyRoute)
fun Screen7() {
}

// param with default value
@RouteBinding(DummyRoute::class)
@Composable
fun SharedTransitionScope.Screen8(
    backStack: NavBackStack<NavKey>,
    route: DummyRoute,
    title: String = "Title",
) {
}

@DependencyGraph(AppScope::class)
interface AppGraph {
    val installers: Set<NavEntryInstaller>
}

fun box(): String {
    val graph = createGraph<AppGraph>()
    assertEquals(
        listOf(
            "Screen1_NavEntryInstaller",
            "Screen2_NavEntryInstaller",
            "Screen3_NavEntryInstaller",
            "Screen4_NavEntryInstaller",
            "Screen5_NavEntryInstaller",
            "Screen6_NavEntryInstaller",
            "Screen7_NavEntryInstaller",
            "Screen8_NavEntryInstaller",
        ),
        graph.installers.mapNotNull { it::class.qualifiedName }.sorted(),
    )
    return "OK"
}
