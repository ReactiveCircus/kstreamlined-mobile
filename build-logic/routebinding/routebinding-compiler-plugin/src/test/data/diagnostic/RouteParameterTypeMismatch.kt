import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import io.github.reactivecircus.routebinding.runtime.RouteBinding

data class AnotherRoute(val id: Int) : NavKey

// matching route type — no error
@RouteBinding(DummyRoute::class)
@Composable
internal fun MatchingRouteType(
    route: DummyRoute,
) {
}

// mismatched route type as value param
@RouteBinding(DummyRoute::class)
@Composable
internal fun MismatchedValueParam(
    <!ROUTE_PARAMETER_TYPE_MISMATCH!>route: AnotherRoute<!>,
) {
}

// mismatched route type as context param
@RouteBinding(DummyRoute::class)
@Composable
context(<!ROUTE_PARAMETER_TYPE_MISMATCH!>route: AnotherRoute<!>)
internal fun MismatchedContextParam() {
}

// mismatched route type as receiver
@RouteBinding(DummyRoute::class)
@Composable
internal fun <!ROUTE_PARAMETER_TYPE_MISMATCH!>AnotherRoute<!>.MismatchedReceiver() {
}
