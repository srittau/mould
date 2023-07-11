package org.rittau.mould.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.runBlocking
import org.rittau.mould.createWorldNote
import org.rittau.mould.loadWorldNotes
import org.rittau.mould.model.Character
import org.rittau.mould.model.WorldNote
import org.rittau.mould.model.WorldNoteType

@Composable
fun NotesPage(character: Character, onOpenNote: (WorldNote, Boolean) -> Unit) {
    val notes = remember {
        val l = mutableStateListOf<WorldNote>()
        l.addAll(prepareNotes())
        l.sortBy { it.title }
        l
    }

    NotesList(character, notes) { note -> onOpenNote(note, false) }
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
