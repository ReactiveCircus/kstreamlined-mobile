package dev.zacsweers.metro

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Inject

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ContributesIntoSet(val scope: kotlin.reflect.KClass<*>)

interface AppScope
