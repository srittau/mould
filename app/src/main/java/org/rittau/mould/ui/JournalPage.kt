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
import org.rittau.mould.createCampaignNote
import org.rittau.mould.loadCampaignNotes
import org.rittau.mould.model.CampaignNote
import org.rittau.mould.model.Character
import java.util.UUID

@Composable
fun JournalPage(character: Character, onOpenJournal: (CampaignNote) -> Unit) {
    val notes = remember {
        val l = mutableStateListOf<CampaignNote>()
        l.addAll(prepareNotes(character.uuid))
        l
    }

    JournalList(notes, onOpenJournal)
    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(modifier = Modifier
            .padding(16.dp)
            .align(Alignment.BottomEnd),
            onClick = {
                val note = runBlocking {
                    createCampaignNote(character.uuid)
                }
                notes.add(note)
                onOpenJournal.invoke(note)
            }) {
            Icon(Icons.Filled.Add, "Add journal entry")
        }
    }
}

private fun prepareNotes(campaignUUID: UUID): MutableList<CampaignNote> {
    return runBlocking {
        loadCampaignNotes(campaignUUID)
    }.toMutableList()
}
