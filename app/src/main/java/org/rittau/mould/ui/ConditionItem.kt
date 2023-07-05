package org.rittau.mould.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ConditionItem(value: Int, selected: Boolean = false, last: Boolean = false) {
    val color = MaterialTheme.colorScheme.outlineVariant
    val text = if (value == 0) "0" else "+$value"
    var modifier = Modifier.width(32.dp)
    if (selected) {
        modifier = modifier.background(MaterialTheme.colorScheme.secondaryContainer)
    }
    if (!last) {
        modifier = modifier.drawBehind {
            drawLine(
                color,
                Offset(size.width, 0f),
                Offset(size.width, size.height),
                .5.dp.toPx(),
            )
        }
    }
    Text(
        text,
        textAlign = TextAlign.Center,
        modifier = modifier.padding(vertical = 2.dp),
    )
}
