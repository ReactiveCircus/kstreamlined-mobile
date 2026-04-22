package io.github.reactivecircus.kstreamlined.kmp.pulse.metro

import dev.zacsweers.metro.MapKey
import io.github.reactivecircus.kstreamlined.kmp.pulse.runtime.Presenter
import kotlin.reflect.KClass

/**
 * A [MapKey] annotation for binding retained presenter types in a multibinding map.
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
public annotation class RetainedKey(val value: KClass<out Presenter<*, *>> = Nothing::class)
