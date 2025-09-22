package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.component

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

internal interface AudioPlayer {
    fun play()

    fun pause()

    fun stop()

    fun seekTo(position: Long)

    fun release()

    val currentPosition: Long

    val duration: Long
}

@OptIn(UnstableApi::class)
@Composable
internal fun rememberAudioPlayer(
    audioUrl: String,
    onPlaybackReady: (currentPosition: Long, duration: Long) -> Unit,
    onPlaybackEnded: (currentPosition: Long) -> Unit,
): AudioPlayer {
    val context = LocalContext.current
    val localInspectionMode = LocalInspectionMode.current
    return remember(audioUrl, context) {
        if (!localInspectionMode) {
            val audioOnlyRenderersFactory = RenderersFactory { handler, _, audioListener, _, _ ->
                arrayOf<Renderer>(
                    MediaCodecAudioRenderer(context, MediaCodecSelector.DEFAULT, handler, audioListener),
                )
            }
            val extractorFactory = ExtractorsFactory {
                arrayOf(Mp3Extractor())
            }
            val exoPlayer = ExoPlayer.Builder(
                context,
                audioOnlyRenderersFactory,
                DefaultMediaSourceFactory(context, extractorFactory),
            ).build().apply {
                setMediaItem(MediaItem.fromUri(audioUrl))
                prepare()
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        when (playbackState) {
                            Player.STATE_READY -> {
                                onPlaybackReady(currentPosition, duration)
                            }

                            Player.STATE_ENDED -> {
                                seekTo(0)
                                onPlaybackEnded(currentPosition)
                            }

                            else -> Unit
                        }
                    }
                })
            }
            RealAudioPlayer(exoPlayer)
        } else {
            object : AudioPlayer {
                override fun play() = Unit

                override fun pause() = Unit

                override fun stop() = Unit

                override fun seekTo(position: Long) = Unit

                override fun release() = Unit

                override val currentPosition: Long = 0

                override val duration: Long = 0
            }
        }
    }
}

internal class RealAudioPlayer(
    private val exoPlayer: ExoPlayer,
) : AudioPlayer {
    override fun play() {
        exoPlayer.play()
    }

    override fun pause() {
        exoPlayer.pause()
    }

    override fun stop() {
        exoPlayer.stop()
    }

    override fun seekTo(position: Long) {
        exoPlayer.seekTo(position)
    }

    override fun release() {
        exoPlayer.release()
    }

    override val currentPosition: Long
        get() = exoPlayer.currentPosition

    override val duration: Long
        get() = exoPlayer.duration
}
