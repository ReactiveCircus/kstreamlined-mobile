import androidx.compose.runtime.Composable
import io.github.reactivecircus.routebinding.runtime.RouteBinding

@RouteBinding(DummyRoute::class)
@Composable
internal fun FooScreen(
    route: DummyRoute,
    <!UNSUPPORTED_VALUE_PARAMETER_TYPE!>foo: String<!>,
    withDefault: Int = 0,
) {
}
