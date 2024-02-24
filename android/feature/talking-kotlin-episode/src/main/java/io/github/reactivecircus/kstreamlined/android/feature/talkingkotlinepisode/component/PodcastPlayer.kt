package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import io.github.reactivecircus.kstreamlined.android.foundation.composeutils.marqueeWithFadedEdges
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.Pause
import io.github.reactivecircus.kstreamlined.kmp.presentation.talkingkotlinepisode.TalkingKotlinEpisode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

@Composable
internal fun PodcastPlayer(
    episode: TalkingKotlinEpisode,
    isPlaying: Boolean,
    onPlayPauseButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    @Suppress("MagicNumber")
    val initialProgressMillis = 1200_000

    val scope = rememberCoroutineScope()
    val playbackController = remember { FakePlaybackController(scope) }
    LaunchedEffect(Unit) {
        playbackController.init(initialProgressMillis)
    }
    DisposableEffect(isPlaying) {
        if (isPlaying) {
            playbackController.play()
        } else {
            playbackController.pause()
        }
        onDispose { }
    }

    val playbackState by playbackController.playbackState.collectAsStateWithLifecycle()

    PodcastPlayerUi(
        playerProgressMillis = playbackState.progressMillis,
        playerDurationMillis = playbackState.durationMillis,
        onProgressChange = { progress ->
            scope.launch {
                playbackController.syncProgress(progress)
            }
        },
        episode = episode,
        isPlaying = isPlaying,
        onPlayPauseButtonClick = onPlayPauseButtonClick,
        modifier = modifier,
        contentPadding = contentPadding,
    )
}

private data class PlaybackState(
    val progressMillis: Int,
    val durationMillis: Int,
)

@OptIn(ExperimentalCoroutinesApi::class)
@Suppress("MagicNumber")
private class FakePlaybackController(scope: CoroutineScope) {
    private val _playbackState = MutableStateFlow(PlaybackState(0, 0))
    val playbackState: StateFlow<PlaybackState> = _playbackState

    private val _initialized = MutableStateFlow(false)
    private val _isPlaying = MutableStateFlow(false)
    private val _syncing = AtomicBoolean(false)

    init {
        scope.launch {
            combine(_initialized, _isPlaying) { initialized, isPlaying ->
                initialized to isPlaying
            }.distinctUntilChanged().flatMapLatest { (initialized, isPlaying) ->
                if (initialized && isPlaying) {
                    flow {
                        while (true) {
                            delay(1000)
                            emit(Unit)
                        }
                    }
                } else {
                    emptyFlow()
                }
            }.collectLatest {
                if (!_syncing.get()) {
                    _playbackState.update {
                        it.copy(progressMillis = (it.progressMillis + 1000).coerceAtMost(it.durationMillis))
                    }
                }
            }
        }
    }

    suspend fun init(initialProgressMillis: Int) {
        delay(500)
        _playbackState.update {
            PlaybackState(initialProgressMillis, 3000_000)
        }
        _initialized.value = true
    }

    suspend fun syncProgress(progressMillis: Int) {
        _syncing.set(true)
        delay(500)
        _playbackState.update {
            it.copy(progressMillis = progressMillis)
        }
        _syncing.set(false)
    }

    fun play() {
        _isPlaying.value = true
    }

    fun pause() {
        _isPlaying.value = false
    }
}

@Composable
internal fun PodcastPlayerUi(
    playerProgressMillis: Int,
    playerDurationMillis: Int,
    onProgressChange: (Int) -> Unit,
    episode: TalkingKotlinEpisode,
    isPlaying: Boolean,
    onPlayPauseButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        color = KSTheme.colorScheme.tertiary,
        contentColor = KSTheme.colorScheme.onTertiary,
    ) {
        Row(
            modifier = Modifier
                .padding(contentPadding)
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AsyncImage(
                model = episode.thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = episode.title,
                    style = KSTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.sp,
                    ),
                    modifier = Modifier
                        .marqueeWithFadedEdges(
                            edgeWidth = 12.dp,
                            iterations = if (isPlaying) Int.MAX_VALUE else 0,
                            delayMillis = 0,
                            velocity = 40.dp,
                        ),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )

                Text(
                    text = episode.displayablePublishTime,
                    style = KSTheme.typography.bodySmall,
                )

                SeekBar(
                    progressMillis = playerProgressMillis,
                    durationMillis = playerDurationMillis,
                    onProgressChangeFinished = onProgressChange,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            AnimatedContent(
                targetState = isPlaying,
                modifier = Modifier.padding(end = 8.dp),
                transitionSpec = { scaleIn() togetherWith scaleOut() },
                contentAlignment = Alignment.Center,
                label = "isPlaying",
            ) { playing ->
                LargeIconButton(
                    if (playing) KSIcons.Pause else KSIcons.PlayArrow,
                    contentDescription = null,
                    onClick = onPlayPauseButtonClick,
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewPodcastPlayerUi_paused() {
    KSTheme {
        Surface {
            PodcastPlayerUi(
                playerProgressMillis = 1200_000,
                playerDurationMillis = 3000_000,
                onProgressChange = {},
                episode = TalkingKotlinEpisode(
                    id = "1",
                    title = "Talking Kotlin Episode Title",
                    displayablePublishTime = "03 Dec 2023",
                    contentUrl = "content-url",
                    savedForLater = false,
                    audioUrl = "audio-url",
                    thumbnailUrl = "podcast-logo-url",
                    summary = "summary",
                    duration = "35min.",
                ),
                isPlaying = false,
                onPlayPauseButtonClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewPodcastPlayer_playing() {
    KSTheme {
        Surface {
            PodcastPlayerUi(
                playerProgressMillis = 1200_000,
                playerDurationMillis = 3000_000,
                onProgressChange = {},
                episode = TalkingKotlinEpisode(
                    id = "1",
                    title = "Talking Kotlin Episode Title",
                    displayablePublishTime = "03 Dec 2023",
                    contentUrl = "content-url",
                    savedForLater = false,
                    audioUrl = "audio-url",
                    thumbnailUrl = "podcast-logo-url",
                    summary = "summary",
                    duration = "35min.",
                ),
                isPlaying = true,
                onPlayPauseButtonClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
