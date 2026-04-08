// ENABLE_COMPOSE_COMPILER

// MODULE: feature-a
package feature.a

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.reactivecircus.routebinding.runtime.RouteBinding

@RouteBinding(FeatureARoute::class)
@Composable
fun SharedTransitionScope.AScreen(
    backStack: NavBackStack<NavKey>,
    route: FeatureARoute,
) {
}

data object FeatureARoute : NavKey

// MODULE: feature-b
package feature.b

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.reactivecircus.routebinding.runtime.RouteBinding

@RouteBinding(FeatureBRoute::class)
@Composable
fun SharedTransitionScope.BScreen(
    backStack: NavBackStack<NavKey>,
    route: FeatureBRoute,
) {
}

data object FeatureBRoute : NavKey

// MODULE: app(feature-a, feature-b)

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.createGraph
import dev.zacsweers.metro.DependencyGraph
import io.github.reactivecircus.routebinding.runtime.RouteBinding
import io.github.reactivecircus.routebinding.runtime.NavEntryInstaller
import kotlin.test.assertEquals

@RouteBinding(MainRoute::class)
@Composable
internal fun SharedTransitionScope.MainScreen(
    backStack: NavBackStack<NavKey>,
    route: MainRoute,
) {
}

data object MainRoute : NavKey

@DependencyGraph(AppScope::class)
interface AppGraph {
    val installers: Set<NavEntryInstaller>
}

fun box(): String {
    val graph = createGraph<AppGraph>()
    assertEquals(
        listOf(
            "MainScreen_NavEntryInstaller",
            "feature.a.AScreen_NavEntryInstaller",
            "feature.b.BScreen_NavEntryInstaller",
        ),
        graph.installers.mapNotNull { it::class.qualifiedName }.sorted(),
    )
    return "OK"
}
