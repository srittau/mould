package org.rittau.mould.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.runBlocking
import org.rittau.mould.deleteCampaign
import org.rittau.mould.model.Campaign
import org.rittau.mould.updateCampaign


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampaignEditor(campaign: Campaign, onClose: () -> Unit, onDelete: (campaign: Campaign) -> Unit) {
    var name by rememberSaveable { mutableStateOf(campaign.name) }
    var changed by rememberSaveable { mutableStateOf(false) }
    var closeDialog by rememberSaveable { mutableStateOf(false) }
    var deleteDialog by rememberSaveable { mutableStateOf(false) }

    fun onSave() {
        campaign.name = name
        runBlocking {
            updateCampaign(campaign)
        }
        changed = false
    }

    fun onDeleteClick() {
        runBlocking { deleteCampaign(campaign.uuid) }
        onDelete(campaign)
    }

    if (closeDialog) {
        AlertDialog(
            onDismissRequest = { closeDialog = false },
            confirmButton = {
                Button(onClick = {
                    closeDialog = false
                    onClose()
                }) {
                    Text("Close")
                }
            },
            dismissButton = {
                Button(onClick = { closeDialog = false }) {
                    Text("Cancel")
                }
            },
            text = { Text("Close without saving?") },
        )
    }

    if (deleteDialog) {
        AlertDialog(
            onDismissRequest = { deleteDialog = false },
            confirmButton = {
                Button(onClick = {
                    deleteDialog = false
                    onDeleteClick()
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { deleteDialog = false }) {
                    Text("Cancel")
                }
            },
            text = { Text("Are you sure you want to delete this campaign?") },
        )
    }

    fun onCloseClick() {
        if (changed) {
            closeDialog = true
        } else {
            onClose()
        }
    }

    val displayTitle = name.ifEmpty { "Untitled campaign" }

    Column {
        TopAppBar(title = {
            Text(
                "Editing $displayTitle", maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }, navigationIcon = {
            IconButton(onClick = { onCloseClick() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Save")
            }
        }, actions = {
            Button(onClick = { onSave() }, enabled = changed) {
                Text("Save")
            }
            IconButton(onClick = { deleteDialog = true }) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete")
            }
        })

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            TextField(
                name,
                label = { Text("Name") },
                singleLine = true,
                onValueChange = { name = it; changed = true },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
