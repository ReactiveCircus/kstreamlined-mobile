import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.reactivecircus.routebinding.runtime.RouteBinding

@RouteBinding(DummyRoute::class)
@Composable
internal fun FooScreen(
    sharedTransitionScope: SharedTransitionScope,
    backStack: NavBackStack<NavKey>,
) {
}

fun box(): String {
    return "OK"
}
