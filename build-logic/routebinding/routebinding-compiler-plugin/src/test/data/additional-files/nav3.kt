package androidx.navigation3.runtime

import androidx.compose.runtime.Composable
import kotlin.reflect.KClass

interface NavKey

class NavBackStack<T : NavKey>

class EntryProviderScope<T : Any> {
    var recordedRouteType: KClass<*>? = null

    // not called by generated code
    fun <K : T> EntryProviderScope<T>.entry(
        key: K,
        contentKey: Any = key.toString(),
        metadata: Map<String, Any> = emptyMap(),
        content: @Composable (K) -> Unit,
    ) {}

    // called by generated code
    inline fun <reified K : T> entry(
        noinline clazzContentKey: (key: @JvmSuppressWildcards K) -> Any = { it.toString() },
        metadata: Map<String, Any> = emptyMap(),
        noinline content: @Composable (K) -> Unit,
    ) {
        recordedRouteType = K::class
    }
}
