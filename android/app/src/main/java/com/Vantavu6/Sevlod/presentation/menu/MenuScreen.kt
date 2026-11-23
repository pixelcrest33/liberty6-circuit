package com.Vantavu6.Sevlod.presentation.menu

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
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
import com.Vantavu6.Sevlod.core.theme.ElectricTeal
import com.Vantavu6.Sevlod.core.theme.HeadlightOrange
import com.Vantavu6.Sevlod.core.theme.Hairline
import com.Vantavu6.Sevlod.core.theme.NeonViolet
import com.Vantavu6.Sevlod.core.theme.SheetFill
import com.Vantavu6.Sevlod.core.theme.TextMuted
import com.Vantavu6.Sevlod.core.theme.TextPrimary
import com.Vantavu6.Sevlod.core.theme.TextSecondary
import com.Vantavu6.Sevlod.core.theme.VioletLight
import com.Vantavu6.Sevlod.core.ui.ChipToggle
import com.Vantavu6.Sevlod.core.ui.PrimaryButton
import com.Vantavu6.Sevlod.core.ui.SecondaryButton
import com.Vantavu6.Sevlod.core.ui.StatCard
import com.Vantavu6.Sevlod.core.ui.TokenBadge
import com.Vantavu6.Sevlod.core.ui.drawCornerBrackets
import com.Vantavu6.Sevlod.presentation.common.CatalogLabels
import com.Vantavu6.Sevlod.presentation.common.Formats

@Composable
fun MenuRoute(
    factory: ViewModelProvider.Factory,
    onPlay: () -> Unit,
    onDistrictSelect: () -> Unit,
    onGarage: () -> Unit,
    onHowToPlay: () -> Unit
) {
    val viewModel: MenuViewModel = viewModel(factory = factory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    MenuScreen(
        state = state,
        onPlay = onPlay,
        onDistrictSelect = onDistrictSelect,
        onGarage = onGarage,
        onHowToPlay = onHowToPlay
    )
}

@Composable
fun MenuScreen(
    state: MenuUiState,
    onPlay: () -> Unit,
    onDistrictSelect: () -> Unit,
    onGarage: () -> Unit,
    onHowToPlay: () -> Unit
) {
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(entered) {
        if (!entered) {
            entered = true
        }
    }
    val sheetOffset by animateDpAsState(
        targetValue = if (entered) 0.dp else 48.dp,
        animationSpec = tween(durationMillis = 420, easing = FastOutSlowInEasing),
        label = "menu_sheet"
    )
    val sheetAlpha by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(durationMillis = 420, easing = FastOutSlowInEasing),
        label = "menu_sheet_alpha"
    )
    val loop = rememberInfiniteTransition(label = "menu_loop")
    val ctaGlow by loop.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "menu_cta_glow"
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.58f)
        ) {
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
                                Color.Transparent,
                                BgDeep.copy(alpha = 0.6f),
                                BgDeep
                            )
                        )
                    )
            )
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .align(Alignment.BottomCenter)
            ) {
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(HeadlightOrange.copy(alpha = 0.34f), Color.Transparent),
                        center = Offset(size.width / 2f, size.height * 0.85f),
                        radius = 260f
                    ),
                    radius = 260f,
                    center = Offset(size.width / 2f, size.height * 0.85f)
                )
            }
            Image(
                painter = painterResource(id = R.drawable.sprite_1),
                contentDescription = stringResource(id = R.string.cd_player_car),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp)
                    .width(168.dp)
                    .height(150.dp)
            )
            TokenBadge(
                count = state.tokens,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(end = 20.dp, top = 8.dp)
            )
        }

        Surface(
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = SheetFill,
            border = BorderStroke(1.dp, Hairline),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .offset(y = sheetOffset)
                .alpha(sheetAlpha)
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(start = 24.dp, end = 24.dp, top = 22.dp, bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(ElectricTeal.copy(alpha = 0.55f))
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                    Text(
                        text = stringResource(id = R.string.brand_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(id = R.string.menu_tagline),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ChipToggle(
                            text = stringResource(id = CatalogLabels.districtName(state.districtId)),
                            selected = true,
                            state = stringResource(id = R.string.state_selected),
                            onClick = onDistrictSelect,
                            modifier = Modifier.weight(1f)
                        )
                        ChipToggle(
                            text = stringResource(id = CatalogLabels.weatherName(state.weatherId)),
                            selected = true,
                            state = stringResource(id = R.string.state_selected),
                            onClick = onDistrictSelect,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (state.bestScore > 0 && state.bestChain > 0) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            StatCard(
                                label = stringResource(id = R.string.stat_best_score),
                                value = Formats.grouped(state.bestScore),
                                accent = ElectricTeal,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                label = stringResource(id = R.string.stat_best_chain),
                                value = Formats.multiplier(state.bestChain),
                                accent = HeadlightOrange,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                    PrimaryButton(
                        text = stringResource(id = R.string.cta_play),
                        onClick = onPlay,
                        colors = listOf(
                            NeonViolet,
                            VioletLight.copy(alpha = ctaGlow + 0.15f)
                        )
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SecondaryButton(
                        text = stringResource(id = R.string.cta_district_select),
                        onClick = onDistrictSelect,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        SecondaryButton(
                            text = stringResource(id = R.string.cta_garage),
                            onClick = onGarage,
                            modifier = Modifier.weight(1f)
                        )
                        SecondaryButton(
                            text = stringResource(id = R.string.cta_how_to_play),
                            onClick = onHowToPlay,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(id = R.string.disclaimer),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        textAlign = TextAlign.Center
                    )
                }
                Canvas(modifier = Modifier.matchParentSize()) {
                    drawCornerBrackets(
                        color = NeonViolet,
                        span = 20.dp.toPx(),
                        stroke = 2.dp.toPx(),
                        inset = 10.dp.toPx()
                    )
                }
            }
        }
    }
}
