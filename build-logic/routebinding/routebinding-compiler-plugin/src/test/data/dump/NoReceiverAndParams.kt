import io.github.reactivecircus.routebinding.runtime.RouteBinding
import androidx.compose.runtime.Composable

@RouteBinding(DummyRoute::class)
@Composable
internal fun FooScreen() {
}

fun box(): String {
    return "OK"
}
