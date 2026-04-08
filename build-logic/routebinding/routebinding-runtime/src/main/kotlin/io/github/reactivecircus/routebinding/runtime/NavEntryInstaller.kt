package io.github.reactivecircus.routebinding.runtime

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

public interface NavEntryInstaller {
    context(entryProviderScope: EntryProviderScope<NavKey>, sharedTransitionScope: SharedTransitionScope)
    public fun install(backStack: NavBackStack<NavKey>)
}
