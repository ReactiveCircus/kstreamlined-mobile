package io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import androidx.tracing.trace
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.LargeIconButton
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.core.designsystem.preview.PreviewKStreamlined
import io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.api.TldrSummaryRoute
import io.github.reactivecircus.routebinding.runtime.RouteBinding

@RouteBinding(TldrSummaryRoute::class)
@Composable
internal fun TldrSummaryScreen(
    backStack: NavBackStack<NavKey>,
    route: TldrSummaryRoute,
) = trace("Screen:TldrSummaryScreen") {
    TldrSummaryScreen(
        title = route.title,
        content = rememberSampleTldrContent(),
        displayableGeneratedTime = SampleGeneratedTime,
        model = SampleModel,
        onNavigateUp = backStack::removeLastOrNull,
        onReadOriginal = { /* TODO */ },
    )
}

@Composable
internal fun TldrSummaryScreen(
    title: String,
    content: AnnotatedString,
    displayableGeneratedTime: String,
    model: String,
    onNavigateUp: () -> Unit,
    onReadOriginal: () -> Unit,
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(KSTheme.colorScheme.background),
    ) {
        HeaderBackdrop(
            modifier = Modifier
                .fillMaxWidth()
                .height(HeaderBackdropHeight),
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Horizontal)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.statusBars.asPaddingValues())
                    .padding(8.dp),
            ) {
                LargeIconButton(
                    KSIcons.Close,
                    contentDescription = null,
                    onClick = onNavigateUp,
                    modifier = Modifier.align(Alignment.CenterStart),
                )
                TldrBadge(modifier = Modifier.align(Alignment.Center))
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom).asPaddingValues())
                    .padding(top = 8.dp, bottom = 72.dp)
                    .padding(horizontal = 24.dp),
            ) {
                Text(
                    text = title,
                    style = KSTheme.typography.titleLarge,
                    color = KSTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.height(12.dp))
                GenerationMetadata(
                    displayableGeneratedTime = displayableGeneratedTime,
                    model = model,
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = content,
                    style = KSTheme.typography.bodyLarge.copy(letterSpacing = 0.1.sp),
                    color = KSTheme.colorScheme.onBackground,
                )
                Spacer(modifier = Modifier.height(24.dp))
                DashedDivider()
                Spacer(modifier = Modifier.height(12.dp))
                Disclosure()
            }
        }
        ReadOriginalButton(
            onClick = onReadOriginal,
            modifier = Modifier.align(Alignment.BottomEnd)
                .padding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom).asPaddingValues())
                .padding(horizontal = 24.dp, vertical = 8.dp),
        )
    }
}

@Composable
private fun HeaderBackdrop(modifier: Modifier = Modifier) {
    val glowColor = KSTheme.colorScheme.surfaceAccent
    val accentColor = KSTheme.colorScheme.accent
    val backgroundColor = KSTheme.colorScheme.background
    Box(
        modifier = modifier.drawBehind {
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(glowColor, Color.Transparent),
                    center = Offset.Zero,
                    radius = size.width,
                ),
            )
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(accentColor.copy(alpha = AccentGlowAlpha), Color.Transparent),
                    center = Offset(size.width, size.height * AccentGlowCenterYFraction),
                    radius = size.width * AccentGlowRadiusFraction,
                ),
            )
            drawRect(
                brush = Brush.verticalGradient(
                    BackdropFadeStartFraction to Color.Transparent,
                    1f to backgroundColor,
                ),
            )
        },
    )
}

private val HeaderBackdropHeight = 320.dp
private const val AccentGlowAlpha = 0.3f
private const val AccentGlowCenterYFraction = 0.15f
private const val AccentGlowRadiusFraction = 0.8f
private const val BackdropFadeStartFraction = 0.6f

@Composable
private fun TldrBadge(modifier: Modifier = Modifier) {
    val brandGradient = KSTheme.colorScheme.brandGradient
    Row(
        modifier = modifier
            .background(
                brush = Brush.horizontalGradient(brandGradient.take(2)),
                shape = CircleShape,
            )
            .padding(start = 8.dp, end = 10.dp)
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            KSIcons.Sparkles,
            contentDescription = null,
            modifier = Modifier.requiredSize(20.dp),
            tint = KSTheme.colorScheme.onAccent,
        )
        Text(
            text = stringResource(R.string.title_tldr_summary),
            style = KSTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.1.sp,
            ),
            color = KSTheme.colorScheme.onAccent,
        )
    }
}

@Composable
private fun GenerationMetadata(
    displayableGeneratedTime: String,
    model: String,
    modifier: Modifier = Modifier,
) {
    val textStyle = KSTheme.typography.labelMedium
    val textColor = KSTheme.colorScheme.onBackgroundMuted
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.tldr_generated_by),
            style = textStyle,
            color = textColor,
        )
        Surface(
            shape = CircleShape,
            color = KSTheme.colorScheme.surfaceAccent,
            contentColor = KSTheme.colorScheme.onSurfaceAccent,
        ) {
            Text(
                text = model,
                style = textStyle.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.1.sp,
                ),
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
            )
        }
        Text(
            text = stringResource(R.string.tldr_generated_on, displayableGeneratedTime),
            style = textStyle,
            color = textColor,
        )
    }
}

@Composable
private fun ReadOriginalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = CircleShape,
        color = KSTheme.colorScheme.surfaceInverse,
        contentColor = KSTheme.colorScheme.onSurfaceInverse,
        elevation = 4.dp,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                KSIcons.ReadMore,
                contentDescription = null,
            )
            Text(
                text = stringResource(R.string.read_original).uppercase(),
                style = KSTheme.typography.labelMedium.copy(fontWeight = FontWeight.ExtraBold),
            )
        }
    }
}

@Composable
private fun DashedDivider(modifier: Modifier = Modifier) {
    val color = KSTheme.colorScheme.borderMuted
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .drawBehind {
                val dash = 4.dp.toPx()
                drawLine(
                    color = color,
                    start = Offset(0f, size.height / 2),
                    end = Offset(size.width, size.height / 2),
                    strokeWidth = size.height,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(dash, dash)),
                )
            },
    )
}

@Composable
private fun Disclosure(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Icon(
            KSIcons.Info,
            contentDescription = null,
            modifier = Modifier.requiredSize(16.dp),
            tint = KSTheme.colorScheme.onBackgroundMuted,
        )
        Text(
            text = stringResource(R.string.tldr_disclosure),
            style = KSTheme.typography.bodySmall,
            color = KSTheme.colorScheme.onBackgroundMuted,
        )
    }
}

@Composable
@PreviewKStreamlined
private fun PreviewTldrSummaryScreen() {
    TldrSummaryScreen(
        title = SampleTitle,
        content = rememberSampleTldrContent(),
        displayableGeneratedTime = SampleGeneratedTime,
        model = SampleModel,
        onNavigateUp = {},
        onReadOriginal = {},
    )
}
