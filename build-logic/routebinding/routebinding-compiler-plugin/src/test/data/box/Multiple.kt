import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.reactivecircus.routebinding.runtime.RouteBinding

@RouteBinding(DummyRoute::class)
@Composable
fun SharedTransitionScope.FooScreen(
    backStack: NavBackStack<NavKey>,
    route: DummyRoute,
) {
}

data object SecondRoute : NavKey

@RouteBinding(SecondRoute::class)
@Composable
fun SharedTransitionScope.BarScreen(
    backStack: NavBackStack<NavKey>,
    route: SecondRoute,
) {
}

fun box(): String {
    return "OK"
}
