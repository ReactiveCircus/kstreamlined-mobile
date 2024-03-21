package io.github.reactivecircus.kstreamlined.android.feature.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.feature.home.R
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.feature.common.R as commonR

@Composable
internal fun TransientErrorBanner(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.padding(8.dp),
        shape = CircleShape,
        color = KSTheme.colorScheme.tertiary,
        contentColor = KSTheme.colorScheme.onTertiary,
        elevation = 4.dp,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painterResource(commonR.drawable.ic_kodee_broken_hearted),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(24.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.transient_error_message),
                style = KSTheme.typography.bodySmall,
            )
            DismissButton(
                onClick = onDismiss,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, end = 8.dp),
            )
        }
    }
}

@Composable
private fun DismissButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .clickable(
                onClick = onClick,
                role = Role.Button,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = KSIcons.Close,
            contentDescription = null,
            modifier = Modifier.requiredSize(20.dp),
        )
    }
}

@Composable
@PreviewLightDark
private fun PreviewTransientErrorBanner() {
    KSTheme {
        Surface {
            TransientErrorBanner(
                onDismiss = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
