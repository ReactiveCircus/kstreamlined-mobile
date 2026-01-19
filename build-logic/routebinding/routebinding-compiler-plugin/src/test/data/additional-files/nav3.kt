package androidx.navigation3.runtime

import androidx.compose.runtime.Composable

interface NavKey

class NavBackStack<T : NavKey>

class EntryProviderScope<T : Any> {
    inline fun <reified K : T> entry(noinline content: @Composable (K) -> Unit) {}
}
