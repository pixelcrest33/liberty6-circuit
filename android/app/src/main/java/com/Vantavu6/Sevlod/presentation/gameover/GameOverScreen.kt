package com.Vantavu6.Sevlod.presentation.gameover

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Vantavu6.Sevlod.R
import com.Vantavu6.Sevlod.core.theme.BgDeep
import com.Vantavu6.Sevlod.core.theme.BgHorizon
import com.Vantavu6.Sevlod.core.theme.ElectricTeal
import com.Vantavu6.Sevlod.core.theme.HeadlightOrange
import com.Vantavu6.Sevlod.core.theme.NeonViolet
import com.Vantavu6.Sevlod.core.theme.SheetFill
import com.Vantavu6.Sevlod.core.theme.TextMuted
import com.Vantavu6.Sevlod.core.theme.TextPrimary
import com.Vantavu6.Sevlod.core.theme.TextSecondary
import com.Vantavu6.Sevlod.core.ui.HudPanel
import com.Vantavu6.Sevlod.core.ui.PrimaryButton
import com.Vantavu6.Sevlod.core.ui.SecondaryButton
import com.Vantavu6.Sevlod.core.ui.StatCard
import com.Vantavu6.Sevlod.domain.model.RunResult
import com.Vantavu6.Sevlod.domain.model.RunVerdict
import com.Vantavu6.Sevlod.presentation.common.CatalogLabels
import com.Vantavu6.Sevlod.presentation.common.Formats

@Composable
fun GameOverRoute(
    factory: ViewModelProvider.Factory,
    result: RunResult,
    onPlayAgain: () -> Unit,
    onDistrictSelect: () -> Unit,
    onMenu: () -> Unit
) {
    val viewModel: GameOverViewModel = viewModel(factory = factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    GameOverScreen(
        result = result,
        state = state,
        onPlayAgain = onPlayAgain,
        onDistrictSelect = onDistrictSelect,
        onMenu = onMenu
    )
}

@Composable
fun GameOverScreen(
    result: RunResult,
    state: GameOverUiState,
    onPlayAgain: () -> Unit,
    onDistrictSelect: () -> Unit,
    onMenu: () -> Unit
) {
    var revealed by remember { mutableStateOf(false) }
    LaunchedEffect(revealed) {
        if (!revealed) {
            revealed = true
        }
    }
    val tokenCount by animateIntAsState(
        targetValue = if (revealed) result.tokens else 0,
        animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing),
        label = "token_count"
    )
    val ribbon = rememberInfiniteTransition(label = "record_ribbon")
    val ribbonSweep by ribbon.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "record_sweep"
    )
    val accent = if (result.verdict == RunVerdict.Crashed) NeonViolet else ElectricTeal

    Box(modifier = Modifier.fillMaxSize()) {
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
                            BgDeep.copy(alpha = 0.95f),
                            BgHorizon.copy(alpha = 0.9f),
                            BgDeep.copy(alpha = 0.95f)
                        )
                    )
                )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(HeadlightOrange.copy(alpha = 0.12f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = CatalogLabels.verdictTitle(result.verdict)),
                style = MaterialTheme.typography.headlineMedium,
                color = accent,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = CatalogLabels.verdictSubtitle(result.verdict)),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            if (result.newRecord) {
                Spacer(modifier = Modifier.height(14.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = HeadlightOrange.copy(alpha = ribbonSweep),
                    border = BorderStroke(1.dp, ElectricTeal),
                    modifier = Modifier.height(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(id = R.string.gameover_record),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextPrimary,
                            modifier = Modifier.padding(horizontal = 14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            HudPanel(
                modifier = Modifier.fillMaxWidth(),
                bracketColor = accent,
                fill = SheetFill
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        label = stringResource(id = R.string.stat_score),
                        value = Formats.grouped(result.score),
                        accent = ElectricTeal,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(id = R.string.stat_distance),
                        value = Formats.metres(result.distanceDp),
                        accent = NeonViolet,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (result.bestChain > 0 && result.tokens > 0) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        StatCard(
                            label = stringResource(id = R.string.stat_best_chain),
                            value = Formats.multiplier(result.bestChain),
                            accent = HeadlightOrange,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            label = stringResource(id = R.string.stat_tokens_run),
                            value = result.tokens.toString(),
                            accent = ElectricTeal,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (result.tokens > 0) {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sprite_2),
                        contentDescription = stringResource(id = R.string.cd_route_token),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.gameover_tokens_banked, tokenCount),
                        style = MaterialTheme.typography.labelSmall,
                        color = ElectricTeal
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.gameover_bank_total, state.totalTokens),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            PrimaryButton(
                text = stringResource(id = R.string.cta_drift_again),
                onClick = onPlayAgain
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondaryButton(
                    text = stringResource(id = R.string.cta_district_select),
                    onClick = onDistrictSelect,
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    text = stringResource(id = R.string.cta_menu),
                    onClick = onMenu,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
