// ENABLE_COMPOSE_COMPILER

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

@RouteBinding(FirstRoute::class)
@Composable
fun SharedTransitionScope.FirstScreen(
    backStack: NavBackStack<NavKey>,
    route: FirstRoute,
) {
}

@RouteBinding(SecondRoute::class)
@Composable
fun SharedTransitionScope.SecondScreen(
    backStack: NavBackStack<NavKey>,
    route: SecondRoute,
) {
}

@DependencyGraph(AppScope::class)
interface AppGraph {
    val installers: Set<NavEntryInstaller>
}

data object FirstRoute : NavKey

data object SecondRoute : NavKey

fun box(): String {
    val graph = createGraph<AppGraph>()
    assertEquals(
        listOf(
            "FirstScreen_NavEntryInstaller",
            "SecondScreen_NavEntryInstaller",
        ),
        graph.installers.map { it.javaClass.canonicalName },
    )
    return "OK"
}
