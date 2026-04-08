// ENABLE_COMPOSE_COMPILER

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
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
fun SharedTransitionScope.TestScreen(
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
    val installer = graph.installers.single()

    val entryProviderScope = EntryProviderScope<NavKey>()
    val sharedTransitionScope = object : SharedTransitionScope {}
    val backStack = NavBackStack<NavKey>()

    with(entryProviderScope) {
        with(sharedTransitionScope) {
            installer.install(backStack)
        }
    }

    assertEquals(DummyRoute::class, entryProviderScope.recordedRouteType)

    return "OK"
}
