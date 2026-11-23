package com.Vantavu6.Sevlod.presentation.game

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Vantavu6.Sevlod.R
import com.Vantavu6.Sevlod.core.theme.ElectricTeal
import com.Vantavu6.Sevlod.core.theme.Hairline
import com.Vantavu6.Sevlod.core.theme.HeadlightOrange
import com.Vantavu6.Sevlod.core.theme.NeonViolet
import com.Vantavu6.Sevlod.core.theme.SheetFill
import com.Vantavu6.Sevlod.core.theme.SurfaceAlt
import com.Vantavu6.Sevlod.core.theme.TextPrimary
import com.Vantavu6.Sevlod.core.theme.VioletHairline

@Composable
fun ControlBar(
    action: ActionLabel,
    controlSideRight: Boolean,
    onLeft: () -> Unit,
    onRight: () -> Unit,
    onAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    val actionText = when (action) {
        ActionLabel.Bank -> stringResource(id = R.string.cta_bank)
        ActionLabel.Nitro -> stringResource(id = R.string.cta_nitro)
        ActionLabel.Drift -> stringResource(id = R.string.cta_drift)
    }
    val actionColors = when (action) {
        ActionLabel.Bank -> listOf(ElectricTeal, NeonViolet)
        ActionLabel.Nitro -> listOf(HeadlightOrange, NeonViolet)
        ActionLabel.Drift -> listOf(NeonViolet, ElectricTeal)
    }
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = SheetFill,
        border = BorderStroke(1.dp, Hairline),
        modifier = modifier.height(84.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (controlSideRight) {
                SteerButton(text = stringResource(id = R.string.cta_right), onClick = onRight)
                ActionButton(text = actionText, colors = actionColors, onClick = onAction)
                SteerButton(text = stringResource(id = R.string.cta_left), onClick = onLeft)
            } else {
                SteerButton(text = stringResource(id = R.string.cta_left), onClick = onLeft)
                ActionButton(text = actionText, colors = actionColors, onClick = onAction)
                SteerButton(text = stringResource(id = R.string.cta_right), onClick = onRight)
            }
        }
    }
}

@Composable
private fun SteerButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = SurfaceAlt,
            contentColor = TextPrimary
        ),
        border = BorderStroke(1.dp, VioletHairline),
        modifier = Modifier
            .width(92.dp)
            .height(56.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = TextPrimary
        )
    }
}

@Composable
private fun RowScope.ActionButton(
    text: String,
    colors: List<Color>,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(14.dp)
    Button(
        onClick = onClick,
        shape = shape,
        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = TextPrimary
        ),
        modifier = Modifier
            .weight(1f)
            .height(56.dp)
            .background(Brush.horizontalGradient(colors), shape)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = TextPrimary
        )
    }
}
