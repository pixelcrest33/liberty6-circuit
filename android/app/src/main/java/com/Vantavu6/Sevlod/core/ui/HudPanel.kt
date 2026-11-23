package com.Vantavu6.Sevlod.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Vantavu6.Sevlod.core.theme.Hairline
import com.Vantavu6.Sevlod.core.theme.NeonViolet
import com.Vantavu6.Sevlod.core.theme.PanelFill

@Composable
fun HudPanel(
    modifier: Modifier = Modifier,
    bracketColor: Color = NeonViolet,
    fill: Color = PanelFill,
    corner: Dp = 16.dp,
    bracketLength: Dp = 20.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    Box(modifier = modifier) {
        Surface(
            shape = RoundedCornerShape(corner),
            color = fill,
            border = BorderStroke(1.dp, Hairline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(contentPadding),
                content = content
            )
        }
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCornerBrackets(bracketColor, bracketLength.toPx(), 2.dp.toPx(), 6.dp.toPx())
        }
    }
}

fun DrawScope.drawCornerBrackets(color: Color, span: Float, stroke: Float, inset: Float) {
    val w = size.width
    val h = size.height
    val corners = listOf(
        Offset(inset, inset) to Offset(inset + span, inset),
        Offset(inset, inset) to Offset(inset, inset + span),
        Offset(w - inset - span, inset) to Offset(w - inset, inset),
        Offset(w - inset, inset) to Offset(w - inset, inset + span),
        Offset(inset, h - inset) to Offset(inset + span, h - inset),
        Offset(inset, h - inset - span) to Offset(inset, h - inset),
        Offset(w - inset - span, h - inset) to Offset(w - inset, h - inset),
        Offset(w - inset, h - inset - span) to Offset(w - inset, h - inset)
    )
    corners.forEach { segment ->
        drawLine(color = color, start = segment.first, end = segment.second, strokeWidth = stroke)
    }
}
