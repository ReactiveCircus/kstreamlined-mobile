import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.reactivecircus.routebinding.runtime.RouteBinding
import io.github.reactivecircus.routebinding.runtime.RouteMetadataProvider

object TestMetadataProvider : RouteMetadataProvider {
    override fun provide(): Map<String, Any> = mapOf("key" to "value")
}

@RouteBinding(DummyRoute::class, metadataProvider = TestMetadataProvider::class)
@Composable
internal fun SharedTransitionScope.FooScreen(
    backStack: NavBackStack<NavKey>,
    route: DummyRoute,
) {
}

fun box(): String {
    return "OK"
}
