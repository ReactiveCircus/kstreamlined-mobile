package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.impl.component

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.retain.RetainedEffect
import androidx.compose.runtime.retain.retain
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.Renderer
import androidx.media3.exoplayer.RenderersFactory
import androidx.media3.exoplayer.audio.MediaCodecAudioRenderer
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.extractor.ExtractorsFactory
import androidx.media3.extractor.mp3.Mp3Extractor
import androidx.media3.ui.compose.state.PlayPauseButtonState
import androidx.media3.ui.compose.state.ProgressStateWithTickInterval
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
): PodcastPlayerState {
    val player = retainAudioPlayer(
        audioUrl = episode.audioUrl,
        startPositionMillis = episode.startPositionMillis,
    )
    val progressState = rememberProgressStateWithTickInterval(
        player = player,
        tickIntervalMs = ProgressTickIntervalMs,
    )
    val playPauseButtonState = rememberPlayPauseButtonState(player)

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
private fun retainAudioPlayer(
    audioUrl: String,
    startPositionMillis: Long,
): Player? {
    if (LocalInspectionMode.current) return null
    val context = LocalContext.current.applicationContext
    val player = retain(audioUrl) {
        val audioOnlyRenderersFactory = RenderersFactory { handler, _, audioListener, _, _ ->
            arrayOf<Renderer>(
                MediaCodecAudioRenderer(context, MediaCodecSelector.DEFAULT, handler, audioListener),
            )
        }
        val extractorFactory = ExtractorsFactory {
            arrayOf(Mp3Extractor())
        }
        ExoPlayer.Builder(
            context,
            audioOnlyRenderersFactory,
            DefaultMediaSourceFactory(context, extractorFactory),
        ).build().apply {
            addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    if (playbackState == Player.STATE_ENDED) {
                        pause()
                        seekTo(0)
                    }
                }
            })
            setMediaItem(MediaItem.fromUri(audioUrl), startPositionMillis)
            prepare()
        }
    }

    RetainedEffect(player) {
        onRetire {
            player.release()
        }
    }

    return player
}
