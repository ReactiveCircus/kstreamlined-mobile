// SNAPSHOT_FUNCTION: /SnapshotTester2.snapshot
// THEME_VARIANT_ENUM: /ThemeVariant2
// CHECK_COMPILER_OUTPUT

class SnapshotTester2 {
    fun snapshot(
        content: @Composable () -> Unit,
    ) {}
}

enum class ThemeVariant2 {
    Light,
    Dark,
}
