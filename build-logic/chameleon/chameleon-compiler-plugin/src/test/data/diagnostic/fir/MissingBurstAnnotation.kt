// SNAPSHOT_FUNCTION: /SnapshotTester.snapshot
// THEME_VARIANT_ENUM: /ThemeVariant

import io.github.reactivecircus.chameleon.runtime.Chameleon

@Chameleon
class <!MISSING_BURST_ANNOTATION!>ComponentTest<!> {
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_Component() {
        snapshotTester.snapshot {
            Component()
        }
    }
}

@Composable
fun Component() {}
