package io.github.reactivecircus.chameleon.runtime

/**
 * Marks a test class for automatic theme variant injection in snapshot function calls.
 * The compiler plugin adds a `themeVariant` property to the class, which enables
 * Burst (https://github.com/cashapp/burst) to generate parameterized tests for each theme variant.
 * The `themeVariant` property is automatically injected into snapshot function calls
 * (e.g., `snapshotTester.snapshot()`).
 * The same class must also be annotated with the `@Burst` annotation.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
public annotation class Chameleon
