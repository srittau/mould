package org.rittau.mould.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.runBlocking
import org.rittau.mould.createWorldNote
import org.rittau.mould.loadWorldNotes
import org.rittau.mould.model.WorldNote
import org.rittau.mould.model.WorldNoteType

@Composable
fun NotesPage(onOpenNote: (WorldNote, Boolean) -> Unit) {
    val notes = remember {
        val l = mutableStateListOf<WorldNote>()
        l.addAll(prepareNotes())
        l.sortBy { it.title }
        l
    }

    NotesList(notes) { note -> onOpenNote(note, false) }
    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd),
            onClick = {
                val note = runBlocking {
                    createWorldNote("", WorldNoteType.Other, "", "")
                }
                notes.add(note)
                onOpenNote.invoke(note, true)
            }) {
            Icon(Icons.Filled.Add, "Add note")
        }
    }
}

private fun prepareNotes(): MutableList<WorldNote> {
    return runBlocking {
        loadWorldNotes()
    }.toMutableList()
}

@Composable
fun NotesList(notes: List<WorldNote>, onClick: (WorldNote) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(notes, key = { note -> note.uuid }) { note ->
            NoteItem(note, onClick)
        }
    }
}

@Composable
fun NoteItem(note: WorldNote, onClick: (WorldNote) -> Unit) {
    ListItem(
        headlineContent = {
            Text(
                if (note.title != "") note.title else "<untitled>",
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        modifier = Modifier.clickable { onClick.invoke(note) },
    )
}
