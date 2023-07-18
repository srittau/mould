package org.rittau.mould.ui

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import kotlinx.coroutines.runBlocking
import org.rittau.mould.createWorldNote
import org.rittau.mould.model.MouldModel
import org.rittau.mould.model.WorldNoteType

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotesPage(model: MouldModel, navigation: MouldNavigation) {
    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = {
                val note = runBlocking {
                    createWorldNote("", WorldNoteType.Other, "", "")
                }
                model.addWorldNote(note)
                navigation.onNoteAdded(note.uuid)
            }) {
            Icon(Icons.Filled.Add, "Add note")
        }
    }) {
        NotesList(model, navigation)
    }
}
