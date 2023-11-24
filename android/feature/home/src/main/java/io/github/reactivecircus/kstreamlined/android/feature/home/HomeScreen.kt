package io.github.reactivecircus.kstreamlined.android.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.common.ui.feed.KotlinWeeklyCard
import io.github.reactivecircus.kstreamlined.android.designsystem.component.FilledIconButton
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.feature.home.component.FeedFilterChip
import io.github.reactivecircus.kstreamlined.android.feature.home.component.SyncButton
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import kotlinx.coroutines.delay

@Composable
public fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(KSTheme.colorScheme.background),
    ) {
        Surface(
            elevation = 2.dp,
        ) {
            // TODO move to :designsystem
            Column(
                modifier = Modifier.padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    GradientTitle(
                        text = "KStreamlined",
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    FilledIconButton(
                        KSIcons.Settings,
                        contentDescription = null,
                        onClick = {},
                        iconTint = KSTheme.colorScheme.primaryOnContainer,
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 24.dp),
                ) {
                    FeedFilterChip(selectedFeedCount = 4)

                    var syncing by remember { mutableStateOf(false) }

                    SyncButton(
                        onClick = { syncing = true },
                        syncing = syncing,
                    )

                    LaunchedEffect(syncing) {
                        if (syncing) {
                            @Suppress("MagicNumber")
                            (delay(500))
                            syncing = false
                        }
                    }
                }
            }
        }

        LazyColumn(
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                Text(
                    text = "Today",
                    style = KSTheme.typography.titleMedium,
                    color = KSTheme.colorScheme.onBackgroundVariant,
                )
            }
            item {
                var item by remember {
                    mutableStateOf(
                        FeedItem.KotlinWeekly(
                            id = "1",
                            title = "Kotlin Weekly #381",
                            publishTime = "Moments ago",
                            contentUrl = "contentUrl",
                            savedForLater = false,
                        )
                    )
                }
                KotlinWeeklyCard(
                    item = item,
                    onItemClick = {},
                    onSaveButtonClick = {
                        item = item.copy(savedForLater = !item.savedForLater)
                    },
                )
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = KSTheme.colorScheme.container,
                ) {}
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = KSTheme.colorScheme.container,
                ) {}
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = KSTheme.colorScheme.container,
                ) {}
            }

            item {
                Text(
                    text = "This week",
                    style = KSTheme.typography.titleMedium,
                    color = KSTheme.colorScheme.onBackgroundVariant,
                )
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = KSTheme.colorScheme.onBackgroundVariant,
                ) {}
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = KSTheme.colorScheme.primary,
                ) {}
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = KSTheme.colorScheme.secondary,
                ) {}
            }

            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = KSTheme.colorScheme.tertiary,
                ) {}
            }
        }
    }
}

@Composable
private fun GradientTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    val gradient = KSTheme.colorScheme.gradient
    val brush = remember {
        object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                return LinearGradientShader(
                    colors = gradient,
                    from = Offset(0f, size.height),
                    to = Offset(size.width * GradientHorizontalScale, 0f),
                )
            }
        }
    }
    Text(
        text = text,
        style = KSTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.ExtraBold,
            brush = brush,
        ),
        modifier = modifier,
        maxLines = 1,
    )
}

private const val GradientHorizontalScale = 1.3f
