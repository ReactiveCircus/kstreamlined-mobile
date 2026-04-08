import androidx.compose.runtime.Composable
import io.github.reactivecircus.routebinding.runtime.RouteBinding
import kotlin.test.assertEquals

@RouteBinding(DummyRoute::class)
@Composable
<!FUNCTION_CANNOT_BE_PRIVATE!>private<!> fun DummyScreen() {
}
