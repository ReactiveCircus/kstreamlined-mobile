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

@RouteBinding(DummyRoute::class)
@Composable
internal fun SharedTransitionScope.DummyScreen(
    backStack: NavBackStack<NavKey>,
    route: DummyRoute,
) {
}

@DependencyGraph(AppScope::class)
interface AppGraph {
    val installers: Set<NavEntryInstaller>
}

fun box(): String {
    val graph = createGraph<AppGraph>()
    assertEquals(
        listOf("DummyScreen_NavEntryInstaller"),
        graph.installers.mapNotNull { it::class.qualifiedName },
    )
    return "OK"
}
