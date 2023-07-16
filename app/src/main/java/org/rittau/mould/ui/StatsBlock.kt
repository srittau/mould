package org.rittau.mould.ui

import androidx.compose.foundation.clickable
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
import org.rittau.mould.model.StatOrTrack

@Composable
fun StatsBlock(character: Character, modifier: Modifier = Modifier, onClick: (StatOrTrack) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(24.dp), modifier = modifier) {
        Stat(character.edge, { onClick(StatOrTrack.Edge) }) { EdgeIcon(modifier = it) }
        Stat(character.heart, { onClick(StatOrTrack.Heart) }) { HeartIcon(modifier = it) }
        Stat(character.iron, { onClick(StatOrTrack.Iron) }) { IronIcon(modifier = it) }
        Stat(character.shadow, { onClick(StatOrTrack.Shadow) }) { ShadowIcon(modifier = it) }
        Stat(character.wits, { onClick(StatOrTrack.Wits) }) { WitsIcon(modifier = it) }
    }
}

@Composable
fun Stat(value: Int, onClick: () -> Unit, icon: @Composable (modifier: Modifier) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onClick() }
    ) {
        icon(modifier = Modifier.size(24.dp))
        Text(value.toString(), fontSize = 24.sp)
    }
}
