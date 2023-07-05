package org.rittau.mould.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
private fun ConditionItemBase(
    text: String,
    selected: Boolean = false,
    reset: Boolean = false,
    last: Boolean = false,
) {
    val color = MaterialTheme.colorScheme.outlineVariant
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
    Box(contentAlignment = Alignment.Center, modifier = modifier.padding(vertical = 2.dp)) {
        if (reset) {
            Icon(
                Icons.Filled.Refresh,
                "Reset",
                tint = Color.LightGray,
                modifier = Modifier.size(22.dp),
            )
        }
        Text(text, textAlign = TextAlign.Center)
    }
}

@Composable
fun ConditionItem(
    value: Int,
    selected: Boolean = false,
    reset: Boolean = false,
    last: Boolean = false,
) {
    val text = if (value == 0) "0" else if (value < 0) value.toString() else "+$value"
    ConditionItemBase(text, selected = selected, reset = reset, last = last)
}

@Composable
fun PlaceholderConditionItem(last: Boolean = false) {
    ConditionItemBase("...", last = last)
}
