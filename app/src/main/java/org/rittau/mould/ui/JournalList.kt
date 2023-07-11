package org.rittau.mould.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.rittau.mould.model.CampaignNote

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
        overlineContent = {
            if (note.date.isNotEmpty()) { Text(note.date) }
        },
        headlineContent = {
            Text(
                if (note.title != "") note.title else "<untitled>",
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        modifier = Modifier.clickable { onClick.invoke(note) },
    )
}
