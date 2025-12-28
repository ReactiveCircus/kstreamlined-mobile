package io.github.reactivecircus.kstreamlined.android.feature.licenses

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import org.junit.Rule
import kotlin.test.Test

@Burst
@Chameleon
class LicensesScreenTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_LicensesScreen() {
        snapshotTester.snapshot {
            LicensesScreenSnapshot(
                uiState = LicensesUiState.Content(
                    items = listOf(
                        ArtifactLicenseItem(
                            title = "Activity",
                            description = "androidx.activity:activity",
                            version = "1.12.0",
                            scmUrl = "https://cs.android.com/androidx/platform/frameworks/support",
                            licenses = listOf("Apache License 2.0"),
                        ),
                        ArtifactLicenseItem(
                            title = "Activity Compose",
                            description = "androidx.activity:activity-compose",
                            version = "1.12.0",
                            scmUrl = "https://cs.android.com/androidx/platform/frameworks/support",
                            licenses = listOf("Apache License 2.0"),
                        ),
                        ArtifactLicenseItem(
                            title = "AndroidX Navigation 3 Runtime",
                            description = "androidx.navigation3:navigation3-runtime",
                            version = "1.0.0",
                            scmUrl = "https://cs.android.com/androidx/platform/frameworks/support",
                            licenses = listOf("Apache License 2.0"),
                        ),
                        ArtifactLicenseItem(
                            title = "AndroidX Navigation 3 UI",
                            description = "androidx.navigation3:navigation3-ui",
                            version = "1.0.0",
                            scmUrl = "https://cs.android.com/androidx/platform/frameworks/support",
                            licenses = listOf("Apache License 2.0"),
                        ),
                        ArtifactLicenseItem(
                            title = "apollo-api",
                            description = "com.apollographql.apollo:apollo-api",
                            version = "5.0.0-alpha.3",
                            scmUrl = "https://github.com/apollographql/apollo-kotlin/",
                            licenses = listOf("MIT License"),
                        ),
                        ArtifactLicenseItem(
                            title = "apollo-runtime",
                            description = "com.apollographql.apollo:apollo-runtime",
                            version = "5.0.0-alpha.3",
                            scmUrl = "https://github.com/apollographql/apollo-kotlin/",
                            licenses = listOf("MIT License"),
                        ),
                        ArtifactLicenseItem(
                            title = "coil",
                            description = "io.coil-kt.coil3:coil",
                            version = "3.3.0",
                            scmUrl = "https://github.com/coil-kt/coil",
                            licenses = listOf("Apache License 2.0"),
                        ),
                    ),
                ),
            )
        }
    }

    @Composable
    internal fun LicensesScreenSnapshot(uiState: LicensesUiState) {
        SharedTransitionLayout {
            AnimatedVisibility(
                visible = true,
                enter = EnterTransition.None,
                exit = ExitTransition.None,
            ) {
                LicensesScreen(
                    onNavigateUp = {},
                    uiState = uiState,
                )
            }
        }
    }
}
