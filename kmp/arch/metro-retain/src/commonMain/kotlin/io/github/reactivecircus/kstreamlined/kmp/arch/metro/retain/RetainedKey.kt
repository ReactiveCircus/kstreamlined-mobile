package io.github.reactivecircus.kstreamlined.kmp.arch.metro.retain

import dev.zacsweers.metro.MapKey
import kotlin.reflect.KClass

/**
 * A [MapKey] annotation for binding retained types in a multibinding map.
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
public annotation class RetainedKey(val value: KClass<out Retainable> = Nothing::class)
