import androidx.compose.runtime.Composable
import io.github.reactivecircus.routebinding.runtime.RouteBinding
import kotlin.test.assertEquals

class Container {
    @RouteBinding(DummyRoute::class)
    @Composable
    internal fun <!FUNCTION_MUST_BE_TOP_LEVEL!>DummyScreen<!>() {
    }
}
