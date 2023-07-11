package org.rittau.mould.ui

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import org.rittau.mould.model.WorldNoteType
import org.rittau.mould.R

@Composable
fun NoteTypeIcon(type: WorldNoteType, modifier: Modifier = Modifier) {
    val icon = when (type) {
        WorldNoteType.World -> R.drawable.world
        WorldNoteType.PC -> R.drawable.pc
        WorldNoteType.NPC -> R.drawable.npc
        WorldNoteType.Faction -> R.drawable.faction
        WorldNoteType.Location -> R.drawable.location
        WorldNoteType.Item -> R.drawable.item
        WorldNoteType.Event -> R.drawable.event
        else -> R.drawable.info
    }
    Icon(painterResource(icon), type.name, modifier = modifier)
}
