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
import org.rittau.mould.model.MouldModel
import org.rittau.mould.model.WorldNote


@Composable
fun NotesList(model: MouldModel, navigation: MouldNavigation) {
    LazyColumn {
        items(model.worldNotes, key = { note -> note.uuid }) { note ->
            NoteItem(model, note, navigation)
        }
    }
}

@Composable
fun NoteItem(model: MouldModel, note: WorldNote, navigation: MouldNavigation) {
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
            if (model.character.isBondedTo(note.uuid)) {
                Icon(
                    painterResource(R.drawable.bond), "Bonded", modifier = Modifier.size(24.dp),
                )
            }
        },
        modifier = Modifier.clickable { navigation.onNoteClicked(note.uuid) },
    )
}
