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
import org.rittau.mould.createCampaignNote
import org.rittau.mould.loadCampaignNotes
import org.rittau.mould.model.CampaignNote

@Composable
fun JournalPage(onOpenJournal: (CampaignNote) -> Unit) {
    val notes = remember {
        val l = mutableStateListOf<CampaignNote>()
        l.addAll(prepareNotes())
        l
    }

    JournalList(notes, onOpenJournal)
    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd),
            onClick = {
                val note = runBlocking {
                    createCampaignNote("", "")
                }
                notes.add(note)
                onOpenJournal.invoke(note)
            }) {
            Icon(Icons.Filled.Add, "Add journal entry")
        }
    }
}

private fun prepareNotes(): MutableList<CampaignNote> {
    return runBlocking {
        loadCampaignNotes()
    }.toMutableList()
}

@Composable
fun JournalList(notes: List<CampaignNote>, onClick: (CampaignNote) -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(notes, key = { note -> note.uuid }) { note ->
            JournalItem(note, onClick)
        }
    }
}

@Composable
fun JournalItem(note: CampaignNote, onClick: (CampaignNote) -> Unit) {
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
