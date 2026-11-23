package com.Vantavu6.Sevlod.presentation.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Vantavu6.Sevlod.R
import com.Vantavu6.Sevlod.domain.model.RunResult
import com.Vantavu6.Sevlod.core.theme.BgDeep
import com.Vantavu6.Sevlod.core.theme.ElectricTeal
import com.Vantavu6.Sevlod.core.theme.Hairline
import com.Vantavu6.Sevlod.core.theme.HeadlightOrange
import com.Vantavu6.Sevlod.core.theme.NeonViolet
import com.Vantavu6.Sevlod.core.theme.PanelFill
import com.Vantavu6.Sevlod.core.theme.SurfaceAlt
import com.Vantavu6.Sevlod.core.theme.TealHairline
import com.Vantavu6.Sevlod.core.theme.TextPrimary
import com.Vantavu6.Sevlod.core.theme.TextSecondary
import com.Vantavu6.Sevlod.core.ui.ChainRail
import com.Vantavu6.Sevlod.core.ui.PrimaryButton
import com.Vantavu6.Sevlod.presentation.common.Formats
import com.Vantavu6.Sevlod.presentation.dialog.PauseDialog

@Composable
fun GameRoute(
    factory: ViewModelProvider.Factory,
    onFinished: (RunResult) -> Unit,
    onMenu: () -> Unit
) {
    val viewModel: GameViewModel = viewModel(factory = factory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.prepareRun()
    }
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is GameEvent.Finished -> onFinished(event.result)
            }
        }
    }
    DisposableEffect(Unit) {
        onDispose { viewModel.pause() }
    }

    GameScreen(
        state = state,
        onBegin = viewModel::start,
        onLeft = viewModel::moveLeft,
        onRight = viewModel::moveRight,
        onAction = viewModel::primaryAction,
        onPause = viewModel::pause,
        onResume = viewModel::resume,
        onRestart = viewModel::restart,
        onMenu = onMenu
    )
}

@Composable
fun GameScreen(
    state: GameUiState,
    onBegin: () -> Unit,
    onLeft: () -> Unit,
    onRight: () -> Unit,
    onAction: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onMenu: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        RoadCanvas(state = state, modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Surface(
                color = PanelFill,
                border = BorderStroke(1.dp, Hairline),
                shape = RoundedCornerShape(bottomStart = 18.dp, bottomEnd = 18.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        onClick = onPause,
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceAlt,
                        border = BorderStroke(1.dp, TealHairline),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Image(
                                painter = painterResource(id = R.drawable.icon_pause),
                                contentDescription = stringResource(id = R.string.cd_pause),
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                    ) {
                        Text(
                            text = stringResource(id = R.string.hud_dawn_in, Formats.clock(state.clockMs)),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .width(160.dp)
                                .height(4.dp)
                                .background(SurfaceAlt, RoundedCornerShape(2.dp))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(state.clockFraction)
                                    .height(4.dp)
                                    .background(
                                        Brush.horizontalGradient(listOf(HeadlightOrange, NeonViolet)),
                                        RoundedCornerShape(2.dp)
                                    )
                            )
                        }
                    }

                    Text(
                        text = Formats.grouped(state.score),
                        style = MaterialTheme.typography.titleLarge,
                        color = if (state.nitroActive) HeadlightOrange else TextPrimary,
                        modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            ChainRail(
                ticks = state.chainTicks,
                multiplier = state.multiplier,
                label = Formats.multiplier(state.multiplier),
                state = stringResource(id = R.string.state_chain, state.multiplier),
                modifier = Modifier
                    .padding(start = 14.dp)
                    .alphaForMiss(state.missFlashMs)
            )
        }

        if (state.hintVisible && (state.phase == GamePhase.Running || state.phase == GamePhase.Border)) {
            ControlHint(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 124.dp, start = 20.dp, end = 20.dp)
            )
        }

        if (state.phase == GamePhase.Running || state.phase == GamePhase.Border) {
            ControlBar(
                action = state.action,
                controlSideRight = state.controlSideRight,
                onLeft = onLeft,
                onRight = onRight,
                onAction = onAction,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(start = 16.dp, end = 16.dp, bottom = 24.dp)
                    .fillMaxWidth()
            )
        }

        if (state.phase == GamePhase.PreRun) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(start = 28.dp, end = 28.dp, bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ControlHint(modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = stringResource(id = R.string.game_ready),
                    style = MaterialTheme.typography.labelSmall,
                    color = ElectricTeal,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                PrimaryButton(
                    text = stringResource(id = R.string.cta_go),
                    onClick = onBegin
                )
            }
        }

        if (state.phase == GamePhase.Paused) {
            PauseDialog(
                distanceText = Formats.metres(state.distanceDp.toInt()),
                onResume = onResume,
                onRestart = onRestart,
                onMenu = onMenu
            )
        }
    }
}

@Composable
private fun ControlHint(modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = BgDeep.copy(alpha = 0.8f),
        border = BorderStroke(1.dp, TealHairline),
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.game_hint),
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
        )
    }
}

private fun Modifier.alphaForMiss(missFlashMs: Float): Modifier =
    this.then(if (missFlashMs > 0f) Modifier.background(SurfaceAlt.copy(alpha = 0.25f)) else Modifier)
