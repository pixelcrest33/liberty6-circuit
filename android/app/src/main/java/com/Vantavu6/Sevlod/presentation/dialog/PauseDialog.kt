package com.Vantavu6.Sevlod.presentation.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.Vantavu6.Sevlod.R
import com.Vantavu6.Sevlod.core.theme.ElectricTeal
import com.Vantavu6.Sevlod.core.theme.SheetFill
import com.Vantavu6.Sevlod.core.theme.TextPrimary
import com.Vantavu6.Sevlod.core.theme.TextSecondary
import com.Vantavu6.Sevlod.core.ui.HudPanel
import com.Vantavu6.Sevlod.core.ui.PrimaryButton
import com.Vantavu6.Sevlod.core.ui.SecondaryButton

@Composable
fun PauseDialog(
    distanceText: String,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onMenu: () -> Unit
) {
    Dialog(onDismissRequest = onResume) {
        HudPanel(
            modifier = Modifier.fillMaxWidth(),
            bracketColor = ElectricTeal,
            fill = SheetFill,
            corner = 20.dp,
            contentPadding = PaddingValues(
                horizontal = 20.dp,
                vertical = 22.dp
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    text = stringResource(id = R.string.pause_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = stringResource(id = R.string.pause_distance, distanceText),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                PrimaryButton(
                    text = stringResource(id = R.string.cta_resume),
                    onClick = onResume
                )
                Spacer(modifier = Modifier.height(10.dp))
                SecondaryButton(
                    text = stringResource(id = R.string.cta_restart),
                    onClick = onRestart,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                SecondaryButton(
                    text = stringResource(id = R.string.cta_menu),
                    onClick = onMenu,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
