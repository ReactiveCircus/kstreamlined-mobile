package io.github.reactivecircus.kstreamlined.android.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Provider
import dev.zacsweers.metro.SingleIn
import io.github.reactivecircus.kstreamlined.kmp.capsule.inject.PresenterFactory
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.Presenter
import io.github.reactivecircus.kstreamlined.kmp.capsule.runtime.PresenterAssistedFactory
import kotlin.reflect.KClass

@Suppress("Unused", "MaxLineLength", "ParameterWrapping")
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class KSPresenterFactory(
    override val presenterProviders: Map<KClass<out Presenter<*, *>>, Provider<Presenter<*, *>>>,
    override val assistedFactoryProviders: Map<KClass<out PresenterAssistedFactory>, Provider<PresenterAssistedFactory>>,
) : PresenterFactory()
