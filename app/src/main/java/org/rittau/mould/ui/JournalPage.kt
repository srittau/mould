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
import org.rittau.mould.createCampaignNote
import org.rittau.mould.loadCampaignNotes
import org.rittau.mould.model.CampaignNote
import org.rittau.mould.model.Character
import java.util.UUID

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun JournalPage(character: Character, onOpenJournal: (CampaignNote) -> Unit) {
    val notes = remember {
        val l = mutableStateListOf<CampaignNote>()
        l.addAll(prepareNotes(character.uuid))
        l
    }

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {
            val note = runBlocking {
                createCampaignNote(character.uuid)
            }
            notes.add(note)
            onOpenJournal.invoke(note)
        }) {
            Icon(Icons.Filled.Add, "Add journal entry")
        }
    }) {
        JournalList(notes, onOpenJournal)
    }
}

private fun prepareNotes(campaignUUID: UUID): MutableList<CampaignNote> {
    return runBlocking {
        loadCampaignNotes(campaignUUID)
    }.toMutableList()
}
