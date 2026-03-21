package io.github.reactivecircus.chameleon.runtime

/**
 * Marks a test class for automatic theme variant injection in snapshot function calls.
 * The compiler plugin adds the `@Burst` annotation and a `themeVariant` property to the class, which enables
 * Burst (https://github.com/cashapp/burst) to generate parameterized tests for each theme variant.
 * The `themeVariant` property is automatically injected into snapshot function calls
 * (e.g., `snapshotTester.snapshot()`).
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
public annotation class Chameleon
