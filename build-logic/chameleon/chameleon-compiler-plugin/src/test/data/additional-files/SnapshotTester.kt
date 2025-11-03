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
