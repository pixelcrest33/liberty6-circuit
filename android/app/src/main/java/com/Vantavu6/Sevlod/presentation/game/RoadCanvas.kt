package com.Vantavu6.Sevlod.presentation.game

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.Vantavu6.Sevlod.R
import com.Vantavu6.Sevlod.core.theme.BgDeep
import com.Vantavu6.Sevlod.core.theme.ElectricTeal
import com.Vantavu6.Sevlod.core.theme.HeadlightOrange
import com.Vantavu6.Sevlod.core.theme.NeonViolet
import com.Vantavu6.Sevlod.core.theme.SurfaceRoad
import com.Vantavu6.Sevlod.core.theme.TextPrimary
import com.Vantavu6.Sevlod.domain.model.RowKind
import kotlin.math.floor

private val RoadFrame = 10.dp
private val CarAnchor = 180.dp
private val CarWidth = 56.dp
private val CarHeight = 96.dp

@Composable
fun RoadCanvas(
    state: GameUiState,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier.clipToBounds()) {
        val roadMaxWidth = min(maxWidth - 32.dp, 420.dp)
        val laneWidth = floor(((roadMaxWidth - RoadFrame * 2) / 3).value).dp
        val roadWidth = laneWidth * 3 + RoadFrame * 2
        val roadLeft = (maxWidth - roadWidth) / 2
        val carCentreY = maxHeight - CarAnchor

        Image(
            painter = painterResource(id = R.drawable.bg_game),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            BgDeep.copy(alpha = 0.9f),
                            BgDeep.copy(alpha = 0.4f),
                            BgDeep.copy(alpha = 0.8f)
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .offset(x = roadLeft)
                .width(roadWidth)
                .fillMaxHeight()
                .background(SurfaceRoad.copy(alpha = 0.72f), RoundedCornerShape(18.dp))
                .border(2.dp, NeonViolet.copy(alpha = 0.25f), RoundedCornerShape(18.dp))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val dash = 26.dp.toPx()
                val gap = 22.dp.toPx()
                val shift = (state.distanceDp % 48f) / 48f * (dash + gap)
                val stroke = 2.dp.toPx()
                listOf(1, 2).forEach { index ->
                    val x = (RoadFrame + laneWidth * index).toPx()
                    var y = -dash + shift
                    while (y < size.height) {
                        drawLine(
                            color = TextPrimary.copy(alpha = 0.12f),
                            start = Offset(x, y),
                            end = Offset(x, y + dash),
                            strokeWidth = stroke
                        )
                        y += dash + gap
                    }
                }
            }
        }

        if (state.nitroActive) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val shift = (state.distanceDp * 2.4f % 160f)
                repeat(14) { index ->
                    val x = roadLeft.toPx() + (index + 1) * (roadWidth.toPx() / 15f)
                    val y = (index * 97f + shift) % size.height
                    drawLine(
                        color = ElectricTeal.copy(alpha = 0.5f),
                        start = Offset(x, y),
                        end = Offset(x, y + 70f),
                        strokeWidth = 2.dp.toPx()
                    )
                }
            }
        }

        val bobOffset = if (state.rows.isEmpty()) {
            0f
        } else {
            val bob = rememberInfiniteTransition(label = "token_bob")
            val bobValue by bob.animateFloat(
                initialValue = -4f,
                targetValue = 4f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 900),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "token_bob_value"
            )
            bobValue
        }

        state.rows.forEach { row ->
            val y = carCentreY - row.offsetDp.dp
            when (row.kind) {
                RowKind.Gate -> {
                    val lane = row.lanes.firstOrNull() ?: 1
                    val centre = roadLeft + RoadFrame + laneWidth * lane + laneWidth / 2
                    val beamColor = if (row.cut) ElectricTeal else NeonViolet
                    Box(
                        modifier = Modifier
                            .offset(x = centre - laneWidth / 2 + 4.dp, y = y - 36.dp)
                            .width(laneWidth - 8.dp)
                            .height(2.dp)
                            .alpha(row.alpha)
                            .background(beamColor)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.sprite_4),
                        contentDescription = stringResource(id = R.string.cd_drift_gate),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .offset(x = centre - laneWidth / 2, y = y - 72.dp)
                            .width(18.dp)
                            .height(72.dp)
                            .alpha(row.alpha)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.sprite_4),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .offset(x = centre + laneWidth / 2 - 18.dp, y = y - 72.dp)
                            .width(18.dp)
                            .height(72.dp)
                            .alpha(row.alpha)
                    )
                }

                RowKind.Token -> {
                    val lane = row.lanes.firstOrNull() ?: 1
                    val centre = roadLeft + RoadFrame + laneWidth * lane + laneWidth / 2
                    Image(
                        painter = painterResource(id = R.drawable.sprite_2),
                        contentDescription = stringResource(id = R.string.cd_route_token),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .offset(x = centre - 18.dp, y = y - 18.dp + bobOffset.dp)
                            .width(36.dp)
                            .height(36.dp)
                            .alpha(row.alpha)
                    )
                }

                RowKind.Cone -> row.lanes.forEach { lane ->
                    val centre = roadLeft + RoadFrame + laneWidth * lane + laneWidth / 2
                    Image(
                        painter = painterResource(id = R.drawable.sprite_3),
                        contentDescription = stringResource(id = R.string.cd_patrol_cone),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .offset(x = centre - 22.dp, y = y - 26.dp)
                            .width(44.dp)
                            .height(52.dp)
                            .alpha(row.alpha)
                    )
                }

                RowKind.Border -> Unit
            }
        }

        if (state.ghostVisible) {
            val ghostCentre = roadLeft + RoadFrame + laneWidth * state.ghostLane + laneWidth / 2
            Image(
                painter = painterResource(id = R.drawable.sprite_1),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(ElectricTeal, BlendMode.Modulate),
                modifier = Modifier
                    .offset(x = ghostCentre - CarWidth / 2, y = carCentreY - CarHeight / 2)
                    .width(CarWidth)
                    .height(CarHeight)
                    .alpha(0.32f)
            )
        }

        val carCentre: Dp = roadLeft + RoadFrame + laneWidth * state.lane + laneWidth / 2
        val animatedCarX by animateDpAsState(
            targetValue = carCentre - CarWidth / 2,
            animationSpec = spring(dampingRatio = 0.78f, stiffness = Spring.StiffnessMedium),
            label = "car_lane"
        )

        Canvas(
            modifier = Modifier
                .offset(x = animatedCarX - 40.dp, y = carCentreY - CarHeight / 2 - 150.dp)
                .width(CarWidth + 80.dp)
                .height(160.dp)
        ) {
            drawRect(
                brush = Brush.verticalGradient(
                    listOf(Color.Transparent, HeadlightOrange.copy(alpha = 0.3f))
                )
            )
        }

        Image(
            painter = painterResource(id = R.drawable.sprite_1),
            contentDescription = stringResource(id = R.string.cd_player_car),
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .offset(x = animatedCarX, y = carCentreY - CarHeight / 2)
                .width(CarWidth)
                .height(CarHeight)
                .graphicsLayer { rotationZ = state.tilt }
        )

        if (state.crashFlash > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(HeadlightOrange.copy(alpha = 0.6f * state.crashFlash))
            )
        }
    }
}
