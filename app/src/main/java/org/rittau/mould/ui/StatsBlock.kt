package org.rittau.mould.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.rittau.mould.model.Character

@Composable
fun StatsBlock(character: Character, modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(24.dp), modifier = modifier) {
        Stat(character.edge) { EdgeIcon(modifier = it) }
        Stat(character.heart) { HeartIcon(modifier = it) }
        Stat(character.iron) { IronIcon(modifier = it) }
        Stat(character.shadow) { ShadowIcon(modifier = it) }
        Stat(character.wits) { WitsIcon(modifier = it) }
    }
}

@Composable
fun Stat(value: Int, icon: @Composable (modifier: Modifier) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon(modifier = Modifier.size(24.dp))
        Text(value.toString(), fontSize = 24.sp)
    }
}
