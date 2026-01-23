import io.github.reactivecircus.routebinding.runtime.RouteBinding
import androidx.compose.runtime.Composable

@RouteBinding(DummyRoute::class)
@Composable
fun FooScreen() {
}

fun box(): String {
    return "OK"
}
