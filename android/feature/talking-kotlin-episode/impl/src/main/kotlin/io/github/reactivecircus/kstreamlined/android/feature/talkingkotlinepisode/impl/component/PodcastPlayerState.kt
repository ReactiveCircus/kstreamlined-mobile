package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.impl.component

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.retain.RetainedEffect
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.state.PlayPauseButtonState
import androidx.media3.ui.compose.state.ProgressStateWithTickInterval
import androidx.media3.ui.compose.state.rememberErrorState
import androidx.media3.ui.compose.state.rememberPlayPauseButtonState
import androidx.media3.ui.compose.state.rememberProgressStateWithTickInterval
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisode

@OptIn(UnstableApi::class)
@Stable
internal class PodcastPlayerState(
    private val player: Player?,
    private val progressState: ProgressStateWithTickInterval,
    private val playPauseButtonState: PlayPauseButtonState,
) {
    val playerPositionMillis: Int
        get() = progressState.currentPositionMs.toPlayerUiMillis()

    val playerDurationMillis: Int
        get() = progressState.durationMs.toPlayerUiMillis()

    val showPauseButton: Boolean
        get() = !playPauseButtonState.showPlay

    val isPlayPauseEnabled: Boolean
        get() = playPauseButtonState.isEnabled

    fun seekTo(positionMillis: Int) {
        player?.seekTo(positionMillis.toLong())
    }

    fun togglePlayPause() {
        playPauseButtonState.onClick()
    }
}

private fun Long.toPlayerUiMillis(): Int = coerceIn(0L, Int.MAX_VALUE.toLong()).toInt()

@OptIn(UnstableApi::class)
@Composable
internal fun rememberPodcastPlayerState(
    episode: TalkingKotlinEpisode,
    onPlayerPositionChange: (Long) -> Unit,
    onPlaybackError: () -> Unit,
): PodcastPlayerState {
    val player = retainPodcastPlayer(
        episode = episode,
    )
    val progressState = rememberProgressStateWithTickInterval(
        player = player,
        tickIntervalMs = ProgressTickIntervalMs,
    )
    val playPauseButtonState = rememberPlayPauseButtonState(player)
    val errorState = rememberErrorState(player)
    val currentOnPlaybackError = rememberUpdatedState(onPlaybackError)

    SideEffect(errorState.error) {
        if (errorState.error != null && player?.playWhenReady == true) {
            currentOnPlaybackError.value()
        }
    }

    val currentOnPlayerPositionChange = rememberUpdatedState(onPlayerPositionChange)
    LaunchedEffect(progressState) {
        snapshotFlow { progressState.currentPositionMs }
            .collect {
                currentOnPlayerPositionChange.value(it)
            }
    }

    return remember(player, progressState, playPauseButtonState) {
        PodcastPlayerState(
            player = player,
            progressState = progressState,
            playPauseButtonState = playPauseButtonState,
        )
    }
}

private const val ProgressTickIntervalMs = 1000L

@OptIn(UnstableApi::class)
@Composable
private fun retainPodcastPlayer(
    episode: TalkingKotlinEpisode,
): Player? {
    if (LocalInspectionMode.current) return null
    val playerFactory = LocalPodcastPlayerFactory.current
    val player = retain(playerFactory, episode.id, episode.audioUrl) {
        playerFactory.create(
            episodeId = episode.id,
            audioUrl = episode.audioUrl,
            startPositionMillis = episode.startPositionMillis,
        )
    }

    RetainedEffect(player) {
        onRetire {
            player.release()
        }
    }

    return player
}
