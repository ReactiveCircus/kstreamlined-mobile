import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.reactivecircus.routebinding.runtime.RouteBinding

data class AnotherRoute(val id: Int) : NavKey

// duplicate NavKey subtype: receiver + value param
@RouteBinding(DummyRoute::class)
@Composable
internal fun DummyRoute.DuplicateNavKeySubtype(
    <!DUPLICATE_PARAMETER_TYPE!>route: AnotherRoute<!>,
) {
}

// duplicate SharedTransitionScope: context param + value param
@RouteBinding(DummyRoute::class)
@Composable
context(sharedTransitionScope: SharedTransitionScope)
internal fun DuplicateSharedTransitionScope(
    <!DUPLICATE_PARAMETER_TYPE!>sharedTransitionScope2: SharedTransitionScope<!>,
) {
}

// duplicate NavBackStack: two value params
@RouteBinding(DummyRoute::class)
@Composable
internal fun DuplicateNavBackStack(
    backStack1: NavBackStack<NavKey>,
    <!DUPLICATE_PARAMETER_TYPE!>backStack2: NavBackStack<NavKey><!>,
) {
}
