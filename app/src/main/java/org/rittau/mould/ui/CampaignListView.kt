package org.rittau.mould.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.runBlocking
import org.rittau.mould.createCampaign
import org.rittau.mould.loadAllCampaigns
import org.rittau.mould.loadCharacter
import org.rittau.mould.loadProgress
import org.rittau.mould.model.Campaign
import org.rittau.mould.model.LoadedCampaign

@Composable
fun CampaignListView(onCampaignSelected: (LoadedCampaign) -> Unit) {
    val campaigns = rememberSaveable {
        val cs = mutableListOf<Campaign>()
        cs.addAll(runBlocking { loadAllCampaigns() })
        cs
    }
    var editedCampaign by rememberSaveable {
        mutableStateOf<Campaign?>(null)
    }

    fun onAdd() {
        val camp = runBlocking { createCampaign() }
        campaigns.add(camp)
        editedCampaign = camp
    }

    if (editedCampaign == null) {
        LazyColumn {
            items(campaigns, key = { c -> c.uuid }) {
                CampaignItem(it, {
                    val character = runBlocking { loadCharacter(it.uuid) }
                    val progress = runBlocking { loadProgress(it.uuid) }
                    onCampaignSelected(LoadedCampaign(character, progress))
                }, { campaign ->
                    editedCampaign = campaign
                })
            }
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
            FloatingActionButton(onClick = { onAdd() }, modifier = Modifier.padding(16.dp)) {
                Icon(Icons.Filled.Add, contentDescription = "Add campaign")
            }
        }
    } else {
        CampaignEditor(editedCampaign!!, {
            editedCampaign = null
        }, {
            campaigns.remove(it)
            editedCampaign = null
        })
    }
}

@Composable
fun CampaignItem(campaign: Campaign, onOpen: (Campaign) -> Unit, onEdit: (Campaign) -> Unit) {
    ListItem(
        headlineContent = {
            Text(campaign.name, maxLines = 1)
        },
        trailingContent = {
            IconButton(onClick = { onEdit(campaign) }) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit campaign")
            }
        },
        modifier = Modifier.clickable { onOpen.invoke(campaign) },
    )
}
