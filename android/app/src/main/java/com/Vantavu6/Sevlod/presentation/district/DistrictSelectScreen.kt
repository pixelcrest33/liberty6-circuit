package com.Vantavu6.Sevlod.presentation.district

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.Vantavu6.Sevlod.core.theme.PanelFill
import com.Vantavu6.Sevlod.core.theme.TextMuted
import com.Vantavu6.Sevlod.core.theme.TextPrimary
import com.Vantavu6.Sevlod.core.theme.TextSecondary
import com.Vantavu6.Sevlod.core.ui.ChipToggle
import com.Vantavu6.Sevlod.core.ui.HudPanel
import com.Vantavu6.Sevlod.core.ui.PrimaryButton
import com.Vantavu6.Sevlod.core.ui.SectionHeader
import com.Vantavu6.Sevlod.core.ui.TokenBadge
import com.Vantavu6.Sevlod.core.ui.TopBackBar
import com.Vantavu6.Sevlod.domain.model.RunMode
import com.Vantavu6.Sevlod.presentation.common.CatalogLabels
import com.Vantavu6.Sevlod.presentation.common.Formats

@Composable
fun DistrictSelectRoute(
    factory: ViewModelProvider.Factory,
    onStartRun: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: DistrictSelectViewModel = viewModel(factory = factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    DistrictSelectScreen(
        state = state,
        onSelectDistrict = viewModel::selectDistrict,
        onSelectWeather = viewModel::selectWeather,
        onSelectMode = viewModel::selectMode,
        onToggleGhost = viewModel::toggleGhost,
        onToggleControlSide = viewModel::toggleControlSide,
        onStartRun = onStartRun,
        onBack = onBack
    )
}

@Composable
fun DistrictSelectScreen(
    state: DistrictSelectUiState,
    onSelectDistrict: (String) -> Unit,
    onSelectWeather: (String) -> Unit,
    onSelectMode: (RunMode) -> Unit,
    onToggleGhost: () -> Unit,
    onToggleControlSide: () -> Unit,
    onStartRun: () -> Unit,
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
                            BgDeep.copy(alpha = 0.94f),
                            BgHorizon.copy(alpha = 0.92f),
                            BgDeep.copy(alpha = 0.96f)
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
                    title = stringResource(id = R.string.screen_district_select),
                    onBack = onBack,
                    modifier = Modifier.weight(1f)
                )
                TokenBadge(count = state.tokens)
            }

            Spacer(modifier = Modifier.height(18.dp))
            SectionHeader(
                title = stringResource(id = R.string.section_routes),
                subtitle = stringResource(id = R.string.section_routes_sub)
            )
            Spacer(modifier = Modifier.height(12.dp))

            state.districts.forEach { card ->
                DistrictCard(
                    card = card,
                    onSelect = { onSelectDistrict(card.id) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader(
                title = stringResource(id = R.string.section_weather),
                subtitle = stringResource(id = R.string.section_weather_sub)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.weatherIds.forEach { id ->
                    ChipToggle(
                        text = stringResource(id = CatalogLabels.weatherName(id)),
                        selected = id == state.selectedWeatherId,
                        state = stringResource(
                            id = if (id == state.selectedWeatherId) {
                                R.string.state_selected
                            } else {
                                R.string.state_not_selected
                            }
                        ),
                        onClick = { onSelectWeather(id) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            SectionHeader(
                title = stringResource(id = R.string.section_mode),
                subtitle = stringResource(id = R.string.section_mode_sub)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RunMode.entries.forEach { mode ->
                    ChipToggle(
                        text = stringResource(id = CatalogLabels.modeName(mode)),
                        selected = mode == state.selectedMode,
                        state = stringResource(
                            id = if (mode == state.selectedMode) {
                                R.string.state_selected
                            } else {
                                R.string.state_not_selected
                            }
                        ),
                        onClick = { onSelectMode(mode) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
            SectionHeader(
                title = stringResource(id = R.string.section_options),
                subtitle = stringResource(id = R.string.section_options_sub)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ChipToggle(
                    text = stringResource(
                        id = if (state.ghostEnabled) R.string.option_ghost_on else R.string.option_ghost_off
                    ),
                    selected = state.ghostEnabled,
                    state = stringResource(
                        id = if (state.ghostEnabled) R.string.state_on else R.string.state_off
                    ),
                    onClick = onToggleGhost,
                    modifier = Modifier.weight(1f)
                )
                ChipToggle(
                    text = stringResource(
                        id = if (state.controlSideRight) {
                            R.string.option_controls_right
                        } else {
                            R.string.option_controls_left
                        }
                    ),
                    selected = true,
                    state = stringResource(
                        id = if (state.controlSideRight) R.string.state_on else R.string.state_off
                    ),
                    onClick = onToggleControlSide,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            PrimaryButton(
                text = stringResource(id = R.string.cta_start_run),
                onClick = onStartRun
            )
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun DistrictCard(
    card: DistrictCardState,
    onSelect: () -> Unit
) {
    val accent = if (card.selected) ElectricTeal else NeonViolet
    Surface(
        onClick = onSelect,
        enabled = card.unlocked,
        shape = RoundedCornerShape(16.dp),
        color = PanelFill,
        border = BorderStroke(1.dp, if (card.unlocked) accent.copy(alpha = 0.45f) else TextMuted),
        modifier = Modifier
            .fillMaxWidth()
            .height(128.dp)
            .alpha(if (card.unlocked) 1f else 0.45f)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Spacer(
                modifier = Modifier
                    .width(4.dp)
                    .fillMaxHeight()
                    .background(accent)
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = CatalogLabels.districtName(card.id)),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(id = CatalogLabels.districtBrief(card.id)),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(10.dp))
                if (card.unlocked) {
                    Text(
                        text = stringResource(id = R.string.district_best, Formats.grouped(card.best)),
                        style = MaterialTheme.typography.labelSmall,
                        color = ElectricTeal
                    )
                } else {
                    Text(
                        text = stringResource(
                            id = R.string.district_locked,
                            Formats.grouped(card.unlockScore)
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        color = HeadlightOrange
                    )
                }
            }
        }
    }
}
