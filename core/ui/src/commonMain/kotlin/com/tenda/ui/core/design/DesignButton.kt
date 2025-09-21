package com.tenda.ui.core.design

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tenda.ui.core.theme.UiTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DesignButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    shape: Shape = RoundedCornerShape(16.dp),
    style: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        fontWeight = FontWeight.SemiBold
    ),
    colors: ButtonColors = ButtonColors(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ),
    elevation: Dp = 0.dp,
    border: BorderStroke? = null,
    minHeight: Dp = 48.dp,
    minWidth: Dp = 64.dp,
    durationMillis: Int = 1000,
    easing: Easing = FastOutSlowInEasing,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    loading: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    val gradient = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondary,
            MaterialTheme.colorScheme.primary,
        )
    )
    val disabledGradient = Brush.horizontalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondaryContainer,
            MaterialTheme.colorScheme.primaryContainer,
        )
    )
    val clickHandler by rememberUpdatedState(onClick)
    val updatedLoading by rememberUpdatedState(loading)
    val updatedContent by rememberUpdatedState(content)
    val contentColor = if (enabled) colors.contentColor else colors.disabledContentColor
    Box(
        modifier = modifier
            .clip(shape)
            .background(brush = if (enabled) {
                gradient
            } else {
                disabledGradient
            })
            .then(if (border != null) {
                Modifier.border(border, shape)
            } else {
                Modifier
            }).shadow(elevation = elevation, shape = shape)
            .clickable(
                role = Role.Button,
                enabled = enabled && !isLoading,
                onClick = { if (!isLoading && enabled) clickHandler() }
            )
            .defaultMinSize(minWidth = minWidth, minHeight = minHeight),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier.padding(contentPadding),
            contentAlignment = Alignment.Center
        ) {
            val infiniteTransition = rememberInfiniteTransition()
            val alpha by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 0.6f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis, easing = easing),
                    repeatMode = RepeatMode.Reverse
                )
            )
            Box(
                modifier = Modifier.graphicsLayer {
                    this.alpha = if (isLoading) alpha else 0f },
            ) {
                CompositionLocalProvider(
                    LocalContentColor provides contentColor,
                    LocalTextStyle provides style.copy(
                        color = contentColor
                    )
                ) { updatedLoading() }
            }
            Box(modifier = Modifier.graphicsLayer {
                this.alpha = if (!isLoading) 1f else 0f }) {
                CompositionLocalProvider(
                    LocalContentColor provides contentColor,
                    LocalTextStyle provides style
                ) { updatedContent() }
            }
        }
    }
}

data class ButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color
)

@Preview
@Composable
fun PreviewDesignButton() {
    UiTheme {
        Column {
            DesignButton(
                onClick = {},
                modifier = Modifier.padding(16.dp)
            ) { Text("Hello, world!") }
            DesignButton(
                onClick = {},
                isLoading = true,
                modifier = Modifier.padding(16.dp),
                loading = { Text("Loading...") }
            ) { Text("Hello, world!") }
            DesignButton(
                onClick = {},
                enabled = false,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .sizeIn(minHeight = 56.dp, minWidth = 200.dp)
            ) { Text("Large Button") }
        }
    }
}
