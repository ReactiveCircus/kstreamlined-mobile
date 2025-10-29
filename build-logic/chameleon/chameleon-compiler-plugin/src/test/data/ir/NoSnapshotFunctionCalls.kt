// SNAPSHOT_FUNCTION: /SnapshotTester.snapshot
// THEME_VARIANT_ENUM: /ThemeVariant

import io.github.reactivecircus.chameleon.runtime.Chameleon

@Chameleon
class ComponentTest {

    @Test
    fun foo() {
        // do not transform when no snapshot function call is present in the class
        Foo()
    }
}

fun Foo() {}
