package org.rittau.mould.ui

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.runBlocking
import org.rittau.mould.createWorldNote
import org.rittau.mould.loadWorldNotes
import org.rittau.mould.model.Character
import org.rittau.mould.model.WorldNote
import org.rittau.mould.model.WorldNoteType

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotesPage(character: Character, onOpenNote: (WorldNote, Boolean) -> Unit) {
    val notes = remember {
        val l = mutableStateListOf<WorldNote>()
        l.addAll(prepareNotes())
        l.sortBy { it.title }
        l
    }

    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = {
                val note = runBlocking {
                    createWorldNote("", WorldNoteType.Other, "", "")
                }
                notes.add(note)
                onOpenNote.invoke(note, true)
            }) {
            Icon(Icons.Filled.Add, "Add note")
        }
    }) {
        NotesList(character, notes) { note -> onOpenNote(note, false) }
    }
}

private fun prepareNotes(): MutableList<WorldNote> {
    return runBlocking {
        loadWorldNotes()
    }.toMutableList()
}
