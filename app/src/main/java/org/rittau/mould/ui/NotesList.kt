package org.rittau.mould.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
    LazyColumn(
        contentPadding = PaddingValues(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
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
        supportingContent = { Text(note.summary, maxLines = 1) },
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
