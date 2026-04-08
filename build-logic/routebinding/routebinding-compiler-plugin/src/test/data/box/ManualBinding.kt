// ENABLE_COMPOSE_COMPILER

// FILE: manual.kt

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import io.github.reactivecircus.routebinding.runtime.NavEntryInstaller

@Composable
internal fun SharedTransitionScope.AScreen(
    backStack: NavBackStack<NavKey>,
    route: ARoute,
) {
}

data object ARoute : NavKey

@ContributesIntoSet(AppScope::class)
class AScreenNavEntryInstaller : NavEntryInstaller {
    context(entryProviderScope: EntryProviderScope<NavKey>, sharedTransitionScope: SharedTransitionScope)
    override fun install(backStack: NavBackStack<NavKey>) {
        entryProviderScope.entry<ARoute> {
            sharedTransitionScope.AScreen(
                backStack = backStack,
                route = it,
            )
        }
    }
}

// FILE: main.kt

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

@RouteBinding(BRoute::class)
@Composable
internal fun SharedTransitionScope.BScreen(
    backStack: NavBackStack<NavKey>,
    route: BRoute,
) {
}

data object BRoute : NavKey

@DependencyGraph(AppScope::class)
interface AppGraph {
    val installers: Set<NavEntryInstaller>
}

fun box(): String {
    val graph = createGraph<AppGraph>()
    assertEquals(
        listOf(
            "AScreenNavEntryInstaller",
            "BScreen_NavEntryInstaller",
        ),
        graph.installers.mapNotNull { it::class.qualifiedName }.sorted(),
    )
    return "OK"
}
