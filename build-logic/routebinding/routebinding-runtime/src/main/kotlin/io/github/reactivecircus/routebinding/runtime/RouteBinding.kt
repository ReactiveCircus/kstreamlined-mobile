package io.github.reactivecircus.routebinding.runtime

import androidx.navigation3.runtime.NavKey
import kotlin.reflect.KClass

// TODO add doc
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
public annotation class RouteBinding(val route: KClass<out NavKey>)
