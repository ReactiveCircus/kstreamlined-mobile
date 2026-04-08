import androidx.compose.runtime.Composable
import io.github.reactivecircus.routebinding.runtime.RouteBinding

@RouteBinding(DummyRoute::class)
@Composable
internal fun FooScreen(
    route: DummyRoute,
) {
}

fun box(): String {
    return "OK"
}
