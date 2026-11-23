package com.Vantavu6.Sevlod.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Vantavu6.Sevlod.R
import com.Vantavu6.Sevlod.core.theme.BgDarker
import com.Vantavu6.Sevlod.core.theme.BgDeep
import com.Vantavu6.Sevlod.core.theme.BgHorizon
import com.Vantavu6.Sevlod.core.theme.ElectricTeal
import com.Vantavu6.Sevlod.core.theme.HeadlightOrange
import com.Vantavu6.Sevlod.core.theme.NeonViolet
import com.Vantavu6.Sevlod.core.theme.SurfaceAlt
import com.Vantavu6.Sevlod.core.theme.TextMuted
import com.Vantavu6.Sevlod.core.theme.TextPrimary
import com.Vantavu6.Sevlod.core.ui.drawCornerBrackets

@Composable
fun SplashRoute(
    factory: ViewModelProvider.Factory,
    onFinished: () -> Unit
) {
    val viewModel: SplashViewModel = viewModel(factory = factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    SplashScreen(finished = state.finished, onFinished = onFinished)
}

@Composable
fun SplashScreen(
    finished: Boolean,
    onFinished: () -> Unit
) {
    LaunchedEffect(finished) {
        if (finished) {
            onFinished()
        }
    }

    var revealed by remember { mutableStateOf(false) }
    LaunchedEffect(revealed) {
        if (!revealed) {
            revealed = true
        }
    }
    val reveal by animateFloatAsState(
        targetValue = if (revealed) 1f else 0f,
        animationSpec = tween(durationMillis = 700, easing = FastOutSlowInEasing),
        label = "splash_reveal"
    )
    val emblemScale by animateFloatAsState(
        targetValue = if (revealed) 1f else 0.86f,
        animationSpec = spring(dampingRatio = 0.62f, stiffness = Spring.StiffnessLow),
        label = "splash_scale"
    )

    val loop = rememberInfiniteTransition(label = "splash_loop")
    val bracketGlow by loop.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "splash_brackets"
    )
    val streakA by loop.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "splash_streak_a"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(BgDarker, BgDeep, BgHorizon))
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(NeonViolet.copy(alpha = 0.22f), Color.Transparent),
                    center = Offset(size.width * 0.5f, size.height * 0.38f),
                    radius = 520f
                ),
                radius = 520f,
                center = Offset(size.width * 0.5f, size.height * 0.38f)
            )
            drawLine(
                color = ElectricTeal.copy(alpha = 0.2f),
                start = Offset(0f, size.height * 0.62f),
                end = Offset(size.width, size.height * 0.62f),
                strokeWidth = 1.dp.toPx()
            )
            val lanes = listOf(0f, 0.34f, 0.68f)
            lanes.forEachIndexed { index, offset ->
                val progress = (streakA + offset) % 1f
                val y = size.height * (0.70f + index * 0.06f)
                val x = size.width * progress
                drawLine(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color.Transparent, HeadlightOrange.copy(alpha = 0.7f), Color.Transparent),
                        startX = x - 120f,
                        endX = x + 120f
                    ),
                    start = Offset(x - 120f, y),
                    end = Offset(x + 120f, y),
                    strokeWidth = 2.dp.toPx()
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier.size(196.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = NeonViolet.copy(alpha = 0.36f),
                        radius = size.minDimension / 2f - 2.dp.toPx(),
                        style = Stroke(width = 1.dp.toPx())
                    )
                    drawCornerBrackets(
                        color = ElectricTeal.copy(alpha = bracketGlow),
                        span = 28.dp.toPx(),
                        stroke = 2.dp.toPx(),
                        inset = 0f
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(148.dp)
                        .alpha(reveal)
                        .graphicsLayer {
                            scaleX = emblemScale
                            scaleY = emblemScale
                        }
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = stringResource(id = R.string.brand_title),
                    style = MaterialTheme.typography.displaySmall,
                    color = NeonViolet,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 2.dp, top = 2.dp)
                        .alpha(reveal)
                )
                Text(
                    text = stringResource(id = R.string.brand_title),
                    style = MaterialTheme.typography.displaySmall,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .alpha(reveal)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(id = R.string.brand_tagline_splash),
                style = MaterialTheme.typography.labelSmall,
                color = ElectricTeal.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(reveal)
            )

            Spacer(modifier = Modifier.height(40.dp))

            LinearProgressIndicator(
                color = ElectricTeal,
                trackColor = SurfaceAlt,
                modifier = Modifier
                    .width(176.dp)
                    .height(3.dp)
                    .background(SurfaceAlt, RoundedCornerShape(2.dp))
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = stringResource(id = R.string.splash_loading),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}
