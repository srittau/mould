package org.rittau.mould.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.rittau.mould.model.Result

@Composable
fun RollResult(result: Result, modifier: Modifier = Modifier) {
    val (name, icon, color) = when (result) {
        Result.StrongHit -> Triple("Strong Hit", Icons.Outlined.CheckCircle, Color(0xFF00CC00))
        Result.WeakHit -> Triple("Weak Hit", Icons.Outlined.Check, Color(0xFFADFF2F))
        Result.Miss -> Triple("Miss", Icons.Outlined.Cancel, Color(0xFFCC0000))
    }
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = modifier) {
        Icon(icon, null, tint = color)
        Text(name)
    }
}

@Composable
fun RollMatch(modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp), modifier = modifier) {
        Icon(Icons.Outlined.Star, null, tint = Color(0xFFFFD700))
        Text("Match!")
    }
}
