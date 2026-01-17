package io.github.reactivecircus.kstreamlined.android.feature.licenses.impl

import androidx.compose.animation.SharedTransitionScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import io.github.reactivecircus.kstreamlined.android.feature.licenses.api.LicensesRoute

context(entryProviderScope: EntryProviderScope<NavKey>, sharedTransitionScope: SharedTransitionScope)
public fun licensesEntry(
    backStack: NavBackStack<NavKey>,
): Unit = entryProviderScope.entry<LicensesRoute> {
    sharedTransitionScope.LicensesScreen(
        backStack = backStack,
    )
}
