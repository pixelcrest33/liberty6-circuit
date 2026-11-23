package com.Vantavu6.Sevlod.presentation.howtoplay

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
import com.Vantavu6.Sevlod.core.theme.TextMuted
import com.Vantavu6.Sevlod.core.theme.TextPrimary
import com.Vantavu6.Sevlod.core.theme.TextSecondary
import com.Vantavu6.Sevlod.core.ui.HudPanel
import com.Vantavu6.Sevlod.core.ui.PrimaryButton
import com.Vantavu6.Sevlod.core.ui.TopBackBar

@Composable
fun HowToPlayRoute(
    factory: ViewModelProvider.Factory,
    onPlay: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: HowToPlayViewModel = viewModel(factory = factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    HowToPlayScreen(state = state, onPlay = onPlay, onBack = onBack)
}

@Composable
fun HowToPlayScreen(
    state: HowToPlayUiState,
    onPlay: () -> Unit,
    onBack: () -> Unit
) {
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
                            BgHorizon.copy(alpha = 0.93f),
                            BgDeep.copy(alpha = 0.97f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            TopBackBar(
                title = stringResource(id = R.string.screen_how_to_play),
                onBack = onBack
            )
            Spacer(modifier = Modifier.height(18.dp))

            state.steps.forEach { step ->
                StepPanel(step = step)
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(id = R.string.disclaimer),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                text = stringResource(id = R.string.cta_play),
                onClick = onPlay
            )
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun StepPanel(step: HowToStep) {
    val titleRes = when (step) {
        HowToStep.Lanes -> R.string.howto_lanes
        HowToStep.Gates -> R.string.howto_gates
        HowToStep.Tokens -> R.string.howto_tokens
        HowToStep.Bank -> R.string.howto_bank
    }
    val bodyRes = when (step) {
        HowToStep.Lanes -> R.string.howto_lanes_body
        HowToStep.Gates -> R.string.howto_gates_body
        HowToStep.Tokens -> R.string.howto_tokens_body
        HowToStep.Bank -> R.string.howto_bank_body
    }
    val iconRes = when (step) {
        HowToStep.Lanes -> R.drawable.sprite_1
        HowToStep.Gates -> R.drawable.sprite_4
        HowToStep.Tokens -> R.drawable.sprite_3
        HowToStep.Bank -> R.drawable.sprite_5
    }
    HudPanel(
        modifier = Modifier.fillMaxWidth(),
        bracketColor = ElectricTeal
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(40.dp)
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = titleRes),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = bodyRes),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
            }
        }
    }
}
