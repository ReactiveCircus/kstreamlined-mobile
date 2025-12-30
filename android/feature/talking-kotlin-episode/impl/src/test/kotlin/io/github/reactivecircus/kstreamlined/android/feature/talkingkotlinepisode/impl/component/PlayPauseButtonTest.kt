package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.impl.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.impl.component.PlayPauseButton
import org.junit.Rule
import org.junit.Test

@Burst
@Chameleon
class PlayPauseButtonTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_PlayPauseButton_paused() {
        snapshotTester.snapshot {
            PlayPauseButton(
                isPlaying = false,
                onPlayPauseButtonClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }

    @Test
    fun snapshot_PlayPauseButton_playing() {
        snapshotTester.snapshot {
            PlayPauseButton(
                isPlaying = true,
                onPlayPauseButtonClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
