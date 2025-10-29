@Retention(AnnotationRetention.RUNTIME)
annotation class Test

@Retention(AnnotationRetention.RUNTIME)
annotation class Rule

@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.TYPE,
    AnnotationTarget.TYPE_PARAMETER,
    AnnotationTarget.PROPERTY_GETTER,
)
annotation class Composable
