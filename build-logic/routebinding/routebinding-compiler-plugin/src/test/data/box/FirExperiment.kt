// LANGUAGE: +ContextParameters

import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.EntryProviderScope
import io.github.reactivecircus.routebinding.runtime.NavEntryInstaller
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.AppScope
import io.github.reactivecircus.routebinding.runtime.RouteBinding

@RouteBinding(DummyRoute::class)
@Composable
fun SharedTransitionScope.FooScreen(
    backStack: NavBackStack<NavKey>,
    route: DummyRoute,
) {
}

//@ContributesIntoSet(AppScope::class)
//class FooScreen_NavEntryInstaller : NavEntryInstaller {
//    ...
//}

fun box(): String {
    return "OK"
}
