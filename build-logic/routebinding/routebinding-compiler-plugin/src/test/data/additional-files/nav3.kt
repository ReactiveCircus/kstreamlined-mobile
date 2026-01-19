package androidx.navigation3.runtime

import androidx.compose.runtime.Composable

interface NavKey

class NavBackStack<T : NavKey>

class EntryProviderScope<T : Any> {
    fun <K : T> EntryProviderScope<T>.entry(content: @Composable (K) -> Unit) {}
}
