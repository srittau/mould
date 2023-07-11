package org.rittau.mould.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.rittau.mould.R
import org.rittau.mould.model.Character
import org.rittau.mould.model.WorldNote


@Composable
fun NotesList(character: Character, notes: List<WorldNote>, onClick: (WorldNote) -> Unit) {
    LazyColumn {
        items(notes, key = { note -> note.uuid }) { note ->
            NoteItem(character, note, onClick)
        }
    }
}

@Composable
fun NoteItem(character: Character, note: WorldNote, onClick: (WorldNote) -> Unit) {
    ListItem(
        headlineContent = {
            Text(
                if (note.title != "") note.title else "<untitled>",
                maxLines = 1,
            )
        },
        supportingContent = { if (note.summary.isNotEmpty()) { Text(note.summary, maxLines = 1) } },
        leadingContent = {
            NoteTypeIcon(note.type, modifier = Modifier.size(24.dp))
        },
        trailingContent = {
            if (character.isBondedTo(note.uuid)) {
                Icon(
                    painterResource(R.drawable.bond), "Bonded", modifier = Modifier.size(24.dp),
                )
            }
        },
        modifier = Modifier.clickable { onClick.invoke(note) },
    )
}
