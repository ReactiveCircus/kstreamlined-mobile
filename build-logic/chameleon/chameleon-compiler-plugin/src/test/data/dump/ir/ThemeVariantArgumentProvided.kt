// SNAPSHOT_FUNCTION: /SnapshotTester.snapshot
// THEME_VARIANT_ENUM: /ThemeVariant

import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon

@Burst
@Chameleon
class ComponentTest {
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_Component_Dark() {
        // do not transform when `ThemeVariant` argument is already provided
        snapshotTester.snapshot(addSurface = false, themeVariant = ThemeVariant.Dark) {
            Component()
        }
    }
}

@Composable
fun Component() {}
