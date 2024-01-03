package io.github.reactivecircus.kstreamlined.android.feature.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import io.github.reactivecircus.kstreamlined.android.common.ui.feed.KotlinBlogCard
import io.github.reactivecircus.kstreamlined.android.common.ui.feed.KotlinWeeklyCard
import io.github.reactivecircus.kstreamlined.android.common.ui.feed.KotlinYouTubeCard
import io.github.reactivecircus.kstreamlined.android.common.ui.feed.TalkingKotlinCard
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Button
import io.github.reactivecircus.kstreamlined.android.designsystem.component.FilledIconButton
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.designsystem.component.TopNavBar
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.feature.home.component.FeedFilterChip
import io.github.reactivecircus.kstreamlined.android.feature.home.component.SyncButton
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.toDisplayable
import io.github.reactivecircus.kstreamlined.kmp.presentation.home.FakeHomeFeedItems
import io.github.reactivecircus.kstreamlined.kmp.presentation.home.HomeFeedItem
import io.github.reactivecircus.kstreamlined.kmp.presentation.home.HomeUiState
import kotlinx.coroutines.delay
import kotlin.random.Random
import io.github.reactivecircus.kstreamlined.android.feature.common.R as CommonR

@Composable
public fun HomeScreen(
    onViewItem: (FeedItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    var syncing by remember { mutableStateOf(true) }

    var uiState: HomeUiState by remember { mutableStateOf(HomeUiState.InFlight) }

    LaunchedEffect(syncing) {
        if (syncing) {
            if (Random.nextBoolean()) {
                uiState = HomeUiState.Content(FakeHomeFeedItems)
            } else {
                uiState = HomeUiState.InFlight
                @Suppress("MagicNumber")
                delay(500)
                uiState = if (Random.nextBoolean()) {
                    HomeUiState.Content(FakeHomeFeedItems)
                } else {
                    HomeUiState.Error
                }
            }
            syncing = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KSTheme.colorScheme.background),
    ) {
        TopNavBar(
            title = stringResource(id = R.string.title_home),
            contentPadding = WindowInsets.statusBars.asPaddingValues(),
            actions = {
                FilledIconButton(
                    KSIcons.Settings,
                    contentDescription = null,
                    onClick = {},
                )
            },
            bottomRow = {
                FeedFilterChip(
                    selectedFeedCount = 4,
                    onClick = {},
                )

                Spacer(modifier = Modifier.width(8.dp))

                SyncButton(
                    onClick = { syncing = true },
                    syncing = syncing,
                )
            }
        )

        Box {
            val transition = updateTransition(targetState = uiState, label = "uiState")
            transition.AnimatedContent(
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                contentAlignment = Alignment.Center,
                contentKey = { state -> state.contentKey }
            ) { state ->
                when (state) {
                    is HomeUiState.InFlight -> {
                        LoadingSkeletonUi()
                    }

                    is HomeUiState.Error -> {
                        ErrorUi(onRetry = { syncing = true })
                    }

                    is HomeUiState.Content -> {
                        ContentUi(
                            items = state.feedItems,
                            onItemClick = onViewItem,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ContentUi(
    items: List<HomeFeedItem>,
    onItemClick: (FeedItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = ListContentPadding,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        items(
            items,
            key = { item ->
                when (item) {
                    is HomeFeedItem.SectionHeader -> item.title
                    is HomeFeedItem.Item -> item.displayableFeedItem.value.id
                }
            },
            contentType = { item ->
                when (item) {
                    is HomeFeedItem.SectionHeader -> HomeFeedItem.SectionHeader::class.simpleName
                    is HomeFeedItem.Item -> item.displayableFeedItem.value::class.simpleName
                }
            },
        ) {
            when (it) {
                is HomeFeedItem.SectionHeader -> {
                    Text(
                        text = it.title,
                        style = KSTheme.typography.titleMedium,
                        color = KSTheme.colorScheme.onBackgroundVariant,
                    )
                }

                is HomeFeedItem.Item -> {
                    val (item, displayablePublishTime) = it.displayableFeedItem
                    when (item) {
                        is FeedItem.KotlinBlog -> {
                            KotlinBlogCard(
                                item = item.toDisplayable(displayablePublishTime),
                                onItemClick = onItemClick,
                                onSaveButtonClick = {},
                            )
                        }

                        is FeedItem.KotlinWeekly -> {
                            KotlinWeeklyCard(
                                item = item.toDisplayable(displayablePublishTime),
                                onItemClick = onItemClick,
                                onSaveButtonClick = {},
                            )
                        }

                        is FeedItem.KotlinYouTube -> {
                            KotlinYouTubeCard(
                                item = item.toDisplayable(displayablePublishTime),
                                onItemClick = onItemClick,
                                onSaveButtonClick = {},
                            )
                        }

                        is FeedItem.TalkingKotlin -> {
                            TalkingKotlinCard(
                                item = item.toDisplayable(displayablePublishTime),
                                onItemClick = onItemClick,
                                onSaveButtonClick = {},
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingSkeletonUi(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        repeat(SkeletonItemCount) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(16.dp),
                color = KSTheme.colorScheme.container,
            ) {}
        }
    }
}

private const val SkeletonItemCount = 10

@Composable
private fun ErrorUi(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(ListContentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        AsyncImage(
            CommonR.drawable.ic_kodee_broken_hearted,
            contentDescription = null,
            modifier = Modifier.size(160.dp),
        )
        Spacer(modifier = Modifier.height(36.dp))
        Text(
            text = stringResource(id = CommonR.string.error_message),
            style = KSTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 24.dp),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            text = "Retry",
            onClick = onRetry,
        )
    }
}

private val ListContentPadding = PaddingValues(
    top = 24.dp,
    start = 24.dp,
    end = 24.dp,
    bottom = 120.dp,
)

private val HomeUiState.contentKey: Int
    get() = when (this) {
        is HomeUiState.InFlight -> 0
        is HomeUiState.Error -> 1
        is HomeUiState.Content -> 2
    }
