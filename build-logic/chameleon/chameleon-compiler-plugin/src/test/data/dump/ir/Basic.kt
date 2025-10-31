// SNAPSHOT_FUNCTION: /SnapshotTester.snapshot
// THEME_VARIANT_ENUM: /ThemeVariant

import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon

@Burst
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

@Composable
fun Component() {}
