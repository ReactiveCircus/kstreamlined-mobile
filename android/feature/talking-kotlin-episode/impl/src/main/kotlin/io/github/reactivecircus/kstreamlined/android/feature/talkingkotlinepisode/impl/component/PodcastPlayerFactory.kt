package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.impl.component

import android.content.Context
import androidx.annotation.OptIn
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.media3.common.AudioAttributes
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.Renderer
import androidx.media3.exoplayer.RenderersFactory
import androidx.media3.exoplayer.audio.MediaCodecAudioRenderer
import androidx.media3.exoplayer.mediacodec.MediaCodecSelector
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.extractor.ExtractorsFactory
import androidx.media3.extractor.mp3.Mp3Extractor
import androidx.media3.extractor.mp4.Mp4Extractor
import androidx.media3.extractor.text.SubtitleParser
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import java.io.File

@OptIn(UnstableApi::class)
@SingleIn(AppScope::class)
@Inject
public class PodcastPlayerFactory(
    private val context: Context,
) {
    private val cache by lazy(LazyThreadSafetyMode.NONE) {
        SimpleCache(
            File(context.cacheDir, CacheDirectoryName),
            LeastRecentlyUsedCacheEvictor(MaxCacheSizeBytes),
            StandaloneDatabaseProvider(context),
        )
    }

    private val cacheDataSourceFactory by lazy(LazyThreadSafetyMode.NONE) {
        CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(DefaultDataSource.Factory(context))
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }

    internal fun create(
        episodeId: String,
        audioUrl: String,
        startPositionMillis: Long,
    ): Player {
        val audioOnlyRenderersFactory = RenderersFactory { handler, _, audioListener, _, _ ->
            arrayOf<Renderer>(
                MediaCodecAudioRenderer(
                    context,
                    MediaCodecSelector.DEFAULT,
                    handler,
                    audioListener,
                ),
            )
        }
        val extractorFactory = ExtractorsFactory {
            arrayOf(
                Mp3Extractor(),
                Mp4Extractor(SubtitleParser.Factory.UNSUPPORTED),
            )
        }
        val mediaSourceFactory = DefaultMediaSourceFactory(
            cacheDataSourceFactory,
            extractorFactory,
        )

        return ExoPlayer.Builder(
            context,
            audioOnlyRenderersFactory,
            mediaSourceFactory,
        )
            .setAudioAttributes(PodcastAudioAttributes, true)
            .setHandleAudioBecomingNoisy(true)
            .build()
            .apply {
                addListener(object : Player.Listener {
                    override fun onPlaybackStateChanged(playbackState: Int) {
                        if (playbackState == Player.STATE_ENDED) {
                            pause()
                            seekTo(0)
                        }
                    }
                })
                setMediaItem(
                    MediaItem.Builder()
                        .setUri(audioUrl)
                        .setCustomCacheKey(episodeId)
                        .build(),
                    startPositionMillis,
                )
                prepare()
            }
    }
}

public val LocalPodcastPlayerFactory: ProvidableCompositionLocal<PodcastPlayerFactory> =
    staticCompositionLocalOf {
        error("No PodcastPlayerFactory registered")
    }

private const val CacheDirectoryName = "talking-kotlin-podcast"
private const val MaxCacheSizeBytes = 300L * 1024 * 1024

private val PodcastAudioAttributes = AudioAttributes.Builder()
    .setUsage(C.USAGE_MEDIA)
    .setContentType(C.AUDIO_CONTENT_TYPE_SPEECH)
    .build()
