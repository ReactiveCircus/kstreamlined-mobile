// SNAPSHOT_FUNCTION: /SnapshotTester.snapshot
// THEME_VARIANT_ENUM: /ThemeVariant

import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon

@Burst
@Chameleon
class ComponentTest(
    val themeVariant: ThemeVariant, // do not transform when a `ThemeVariant` property is already present
) {
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_Component() {
        snapshotTester.snapshot(themeVariant = themeVariant) {
            Component()
        }
    }
}

@Composable
fun Component() {}
