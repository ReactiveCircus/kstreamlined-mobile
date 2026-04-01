// LANGUAGE: +ContextParameters
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import io.github.reactivecircus.routebinding.runtime.RouteBinding

@RouteBinding(DummyRoute::class)
@Composable
context(sharedTransitionScope: SharedTransitionScope, <!UNSUPPORTED_CONTEXT_PARAMETER_TYPE!>foo: String<!>)
internal fun Foo() {
}
