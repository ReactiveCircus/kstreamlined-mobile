// SNAPSHOT_FUNCTION: /SnapshotTester.snapshot
// THEME_VARIANT_ENUM: /ThemeVariant

import io.github.reactivecircus.chameleon.runtime.Chameleon

@Chameleon
class ComponentTest {
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_Component() {
        snapshotTester.snapshot {
            Component()
        }
    }
}

@Retention(AnnotationRetention.RUNTIME)
annotation class Test

@Retention(AnnotationRetention.RUNTIME)
annotation class Rule

class SnapshotTester {
    fun snapshot(
        addSurface: Boolean = true,
        themeVariant: ThemeVariant = ThemeVariant.Light,
        content: @Composable () -> Unit,
    ) {}
}

enum class ThemeVariant {
    Light,
    Dark,
}

@Retention(AnnotationRetention.BINARY)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.TYPE,
    AnnotationTarget.TYPE_PARAMETER,
    AnnotationTarget.PROPERTY_GETTER,
)
annotation class Composable

@Composable
fun Component() {}
