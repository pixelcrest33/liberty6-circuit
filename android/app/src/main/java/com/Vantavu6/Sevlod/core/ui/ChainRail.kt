package com.Vantavu6.Sevlod.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Vantavu6.Sevlod.core.theme.ElectricTeal
import com.Vantavu6.Sevlod.core.theme.HeadlightOrange
import com.Vantavu6.Sevlod.core.theme.NeonViolet
import com.Vantavu6.Sevlod.core.theme.SurfaceAlt
import com.Vantavu6.Sevlod.core.theme.TextPrimary

@Composable
fun ChainRail(
    ticks: Int,
    multiplier: Int,
    label: String,
    state: String,
    modifier: Modifier = Modifier
) {
    val filled = ticks.coerceIn(0, SEGMENTS)
    val tierColor = when {
        multiplier >= 7 -> HeadlightOrange
        multiplier >= 4 -> ElectricTeal
        else -> NeonViolet
    }
    Row(
        modifier = modifier.semantics { stateDescription = state },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier
                .width(8.dp)
                .height(220.dp),
            verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            repeat(SEGMENTS) { index ->
                val lit = SEGMENTS - index <= filled
                Spacer(
                    modifier = Modifier
                        .width(8.dp)
                        .height(SEGMENT_HEIGHT.dp)
                        .background(
                            color = if (lit) tierColor else SurfaceAlt,
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = if (filled > 0) tierColor else TextPrimary.copy(alpha = 0.5f)
        )
    }
}

private const val SEGMENTS = 21
private const val SEGMENT_HEIGHT = 7
