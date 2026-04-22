package io.github.reactivecircus.kstreamlined.kmp.capsule.runtime

import dev.zacsweers.metro.MapKey
import kotlin.reflect.KClass

/**
 * A [MapKey] annotation for binding [PresenterAssistedFactory] instances in a multibinding map.
 */
@MapKey(implicitClassKey = true)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.FIELD,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.CLASS,
    AnnotationTarget.TYPE,
)
@Retention(AnnotationRetention.RUNTIME)
public annotation class PresenterAssistedFactoryKey(
    val value: KClass<out PresenterAssistedFactory> = Nothing::class,
)
