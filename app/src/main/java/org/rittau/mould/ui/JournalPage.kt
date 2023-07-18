package org.rittau.mould.ui

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import kotlinx.coroutines.runBlocking
import org.rittau.mould.createCampaignNote
import org.rittau.mould.model.MouldModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun JournalPage(model: MouldModel, navigation: MouldNavigation) {
    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {
            val entry = runBlocking {
                createCampaignNote(model.character.uuid)
            }
            model.addJournalEntry(entry)
            navigation.onJournalEntryAdded(entry.uuid)
        }) {
            Icon(Icons.Filled.Add, "Add journal entry")
        }
    }) {
        JournalList(model, navigation)
    }
}
