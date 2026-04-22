package io.github.reactivecircus.kstreamlined.kmp.capsule.runtime

import dev.zacsweers.metro.MapKey
import kotlin.reflect.KClass

/**
 * A [MapKey] annotation for binding [Presenter] types in a multibinding map.
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
public annotation class PresenterKey(val value: KClass<out Presenter<*, *>> = Nothing::class)
