package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.impl.component

import androidx.annotation.OptIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.retain.retain
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

@OptIn(UnstableApi::class)
@Composable
internal fun retainAudioPlayer(
    audioUrl: String,
    startPositionMillis: Long,
    onPlaybackEnded: () -> Unit,
): Player? {
    val context = LocalContext.current.applicationContext
    val inspectionMode = LocalInspectionMode.current
    val currentOnPlaybackEnded = rememberUpdatedState(onPlaybackEnded)
    return retain(audioUrl) {
        if (!inspectionMode) {
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
                            seekTo(0)
                            currentOnPlaybackEnded.value()
                        }
                    }
                })
                setMediaItem(MediaItem.fromUri(audioUrl), startPositionMillis)
                prepare()
            }
        } else {
            null
        }
    }
}
