import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import io.github.reactivecircus.routebinding.runtime.RouteBinding
import io.github.reactivecircus.routebinding.runtime.RouteMetadataProvider

class ClassMetadataProvider : RouteMetadataProvider {
    override fun provide(): Map<String, Any> = mapOf("key" to "value")
}

@RouteBinding(DummyRoute::class, metadataProvider = <!METADATA_PROVIDER_MUST_BE_OBJECT!>ClassMetadataProvider::class<!>)
@Composable
internal fun Screen1() {
}

@RouteBinding(DummyRoute::class, metadataProvider = <!METADATA_PROVIDER_MUST_BE_OBJECT!>RouteMetadataProvider::class<!>)
@Composable
internal fun Screen2() {
}
