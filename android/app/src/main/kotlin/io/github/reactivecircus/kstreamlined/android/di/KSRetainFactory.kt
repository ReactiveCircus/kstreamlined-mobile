package io.github.reactivecircus.kstreamlined.android.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Provider
import dev.zacsweers.metro.SingleIn
import io.github.reactivecircus.kstreamlined.kmp.arch.metro.retain.MetroRetainFactory
import io.github.reactivecircus.kstreamlined.kmp.arch.metro.retain.Retainable
import io.github.reactivecircus.kstreamlined.kmp.arch.metro.retain.RetainedAssistedFactory
import kotlin.reflect.KClass

@Suppress("Unused")
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class KSRetainFactory(
    override val retainedProviders: Map<KClass<out Retainable>, Provider<Retainable>>,
    override val assistedFactoryProviders: Map<KClass<out RetainedAssistedFactory>, Provider<RetainedAssistedFactory>>,
) : MetroRetainFactory()
