package com.Vantavu6.Sevlod.presentation.garage

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Vantavu6.Sevlod.R
import com.Vantavu6.Sevlod.core.theme.BgDeep
import com.Vantavu6.Sevlod.core.theme.BgHorizon
import com.Vantavu6.Sevlod.core.theme.ElectricTeal
import com.Vantavu6.Sevlod.core.theme.NeonViolet
import com.Vantavu6.Sevlod.core.theme.PanelFill
import com.Vantavu6.Sevlod.core.theme.SurfaceAlt
import com.Vantavu6.Sevlod.core.theme.TextMuted
import com.Vantavu6.Sevlod.core.theme.TextPrimary
import com.Vantavu6.Sevlod.core.theme.TextSecondary
import com.Vantavu6.Sevlod.core.ui.SecondaryButton
import com.Vantavu6.Sevlod.core.ui.SectionHeader
import com.Vantavu6.Sevlod.core.ui.TokenBadge
import com.Vantavu6.Sevlod.core.ui.TopBackBar
import com.Vantavu6.Sevlod.presentation.common.CatalogLabels
import com.Vantavu6.Sevlod.presentation.common.Formats

@Composable
fun GarageRoute(
    factory: ViewModelProvider.Factory,
    onBack: () -> Unit
) {
    val viewModel: GarageViewModel = viewModel(factory = factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    GarageScreen(
        state = state,
        onInstall = viewModel::install,
        onBack = onBack
    )
}

@Composable
fun GarageScreen(
    state: GarageUiState,
    onInstall: (String) -> Unit,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TopBackBar(
                    title = stringResource(id = R.string.screen_garage),
                    onBack = onBack,
                    modifier = Modifier.weight(1f)
                )
                TokenBadge(count = state.tokens)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(NeonViolet.copy(alpha = 0.3f), Color.Transparent),
                            center = Offset(size.width / 2f, size.height * 0.72f),
                            radius = 300f
                        ),
                        radius = 300f,
                        center = Offset(size.width / 2f, size.height * 0.72f)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.sprite_1),
                    contentDescription = stringResource(id = R.string.cd_player_car),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(180.dp)
                        .height(180.dp)
                )
            }

            if (state.installedPartId.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = ElectricTeal.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, ElectricTeal),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.garage_installed),
                        style = MaterialTheme.typography.labelSmall,
                        color = ElectricTeal,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            SectionHeader(
                title = stringResource(id = R.string.section_tuning),
                subtitle = stringResource(
                    id = if (state.tokens > 0) R.string.section_tuning_sub else R.string.garage_empty
                )
            )
            Spacer(modifier = Modifier.height(12.dp))

            state.rows.forEach { row ->
                TuningRow(row = row, onInstall = { onInstall(row.id) })
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.disclaimer),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun TuningRow(
    row: TuningRowState,
    onInstall: () -> Unit
) {
    val fill by animateFloatAsState(
        targetValue = row.level.toFloat() / row.maxLevel.toFloat(),
        animationSpec = tween(durationMillis = 420, easing = FastOutSlowInEasing),
        label = "tuning_fill"
    )
    val levelState = stringResource(id = R.string.state_part_level, row.level, row.maxLevel)
    val buttonText = when {
        row.maxed -> stringResource(id = R.string.cta_max_level)
        row.affordable -> stringResource(id = R.string.cta_install)
        else -> stringResource(id = R.string.cta_need_tokens, Formats.grouped(row.nextCost))
    }
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = PanelFill,
        border = BorderStroke(1.dp, if (row.affordable) ElectricTeal.copy(alpha = 0.5f) else TextMuted),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.sprite_5),
                contentDescription = stringResource(id = R.string.cd_tuning_part),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(32.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(id = CatalogLabels.partName(row.id)),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(id = CatalogLabels.partEffect(row.id)),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(SurfaceAlt, RoundedCornerShape(3.dp))
                        .semantics { stateDescription = levelState }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fill)
                            .height(6.dp)
                            .background(ElectricTeal, RoundedCornerShape(3.dp))
                    )
                }
            }
            SecondaryButton(
                text = buttonText,
                onClick = onInstall,
                enabled = row.affordable,
                borderColor = if (row.affordable) ElectricTeal else TextMuted,
                contentColor = if (row.affordable) ElectricTeal else TextMuted,
                modifier = Modifier.width(118.dp)
            )
        }
    }
}
