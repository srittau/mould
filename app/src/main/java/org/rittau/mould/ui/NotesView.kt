package org.rittau.mould.ui

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.runBlocking
import org.rittau.mould.createNote
import org.rittau.mould.loadNotes
import org.rittau.mould.model.CampaignNote
import org.rittau.mould.ui.theme.MouldTheme

@Composable
fun NotesView(onOpenNote: (CampaignNote) -> Unit) {
    val notes = remember {
        val l = mutableStateListOf<CampaignNote>()
        l.addAll(prepareNotes())
        l.sortBy { it.title }
        l
    }

    NotesList(notes, onOpenNote)
    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd),
            onClick = {
                val note = runBlocking {
                    createNote("", "")
                }
                notes.add(note)
                onOpenNote.invoke(note)
            }) {
            Icon(Icons.Filled.Add, "Add note")
        }
    }
}

private fun prepareNotes(): MutableList<CampaignNote> {
    return runBlocking {
        loadNotes()
    }.toMutableList()
}

@Composable
fun NotesList(notes: List<CampaignNote>, onClick: (CampaignNote) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(notes, key = { note -> note.uuid }) { note ->
            NoteItem(note, onClick)
        }
    }
}

@Composable
fun NoteItem(note: CampaignNote, onClick: (CampaignNote) -> Unit) {
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

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NotesViewPreview() {
    MouldTheme {
        NotesView {}
    }
}
