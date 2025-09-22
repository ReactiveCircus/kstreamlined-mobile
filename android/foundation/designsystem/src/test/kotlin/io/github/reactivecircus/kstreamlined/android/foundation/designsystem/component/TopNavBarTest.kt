package io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.ThemeVariantInjector
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalSharedTransitionApi::class)
@RunWith(ThemeVariantInjector::class)
class TopNavBarTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_TopNavBar_default() {
        snapshotTester.snapshot {
            SharedTransitionLayout {
                TopNavBar(
                    title = "Title",
                    actions = {
                        FilledIconButton(
                            KSIcons.Settings,
                            contentDescription = null,
                            onClick = {},
                        )
                    },
                )
            }
        }
    }

    @Test
    fun snapshot_TopNavBar_withBottomRow() {
        snapshotTester.snapshot {
            SharedTransitionLayout {
                TopNavBar(
                    title = "Title",
                    actions = {
                        FilledIconButton(
                            KSIcons.Settings,
                            contentDescription = null,
                            onClick = {},
                        )
                    },
                    bottomRow = {
                        Chip(
                            onClick = {},
                            contentColor = KSTheme.colorScheme.primary,
                        ) {
                            Text(
                                text = "Button".uppercase(),
                                style = KSTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.sp,
                                ),
                            )
                            Icon(KSIcons.ArrowDown, contentDescription = null)
                        }
                    },
                )
            }
        }
    }

    @Test
    fun snapshot_TopNavBar_withNavigationIcon() {
        snapshotTester.snapshot {
            SharedTransitionLayout {
                TopNavBar(
                    title = "Title",
                    navigationIcon = {
                        LargeIconButton(
                            KSIcons.Close,
                            contentDescription = null,
                            onClick = {},
                        )
                    },
                )
            }
        }
    }
}
