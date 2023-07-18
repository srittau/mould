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
import org.rittau.mould.model.MouldModel

@Composable
fun JournalList(model: MouldModel, navigation: MouldNavigation) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(model.journal, key = { entry -> entry.uuid }) { entry ->
            JournalItem(entry, navigation)
        }
    }
}

@Composable
fun JournalItem(entry: CampaignNote, navigation: MouldNavigation) {
    ListItem(
        overlineContent = {
            if (entry.date.isNotEmpty()) { Text(entry.date) }
        },
        headlineContent = {
            Text(
                if (entry.title != "") entry.title else "<untitled>",
                style = MaterialTheme.typography.bodyLarge,
            )
        },
        modifier = Modifier.clickable { navigation.onJournalEntryClicked(entry.uuid) },
    )
}
