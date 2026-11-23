package com.Vantavu6.Sevlod.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Vantavu6.Sevlod.R
import com.Vantavu6.Sevlod.core.theme.ElectricTeal
import com.Vantavu6.Sevlod.core.theme.PanelFill
import com.Vantavu6.Sevlod.core.theme.TealHairline

@Composable
fun TokenBadge(
    count: Int,
    modifier: Modifier = Modifier
) {
    if (count <= 0) return
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = PanelFill,
        border = BorderStroke(1.dp, TealHairline),
        modifier = modifier.height(40.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.sprite_2),
                contentDescription = stringResource(id = R.string.cd_route_token),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = ElectricTeal
            )
        }
    }
}
