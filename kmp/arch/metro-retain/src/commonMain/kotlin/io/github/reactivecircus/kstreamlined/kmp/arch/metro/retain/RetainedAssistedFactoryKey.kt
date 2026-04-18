package io.github.reactivecircus.kstreamlined.kmp.arch.metro.retain

import dev.zacsweers.metro.MapKey
import kotlin.reflect.KClass

/**
 * A [MapKey] annotation for binding [RetainedAssistedFactory] instances in a multibinding map.
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
public annotation class RetainedAssistedFactoryKey(
    val value: KClass<out RetainedAssistedFactory> = Nothing::class,
)
