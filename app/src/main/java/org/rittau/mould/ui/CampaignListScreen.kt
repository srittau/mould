package org.rittau.mould.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import kotlinx.coroutines.runBlocking
import org.rittau.mould.createCampaign
import org.rittau.mould.loadCharacter
import org.rittau.mould.model.Campaign
import org.rittau.mould.model.MouldModel

class CampaignListScreen(val model: MouldModel, val navigation: MouldNavigation) : MouldScreen() {
    override val screen = MouldScreenType.CampaignList

    override val hasNavBar = false

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    override fun Content() {
        val campaignUUID = navigation.selectedCampaign
        if (campaignUUID == null) {
            LazyColumn {
                items(model.campaigns, key = { c -> c.uuid }) {
                    CampaignItem(it, navigation) { campaign ->
                        loadCharacter(model, campaign.uuid)
                        navigation.onCharacterOpened()
                    }
                }
            }
        } else {
            CampaignEditor(model, navigation)
        }
    }

    @Composable
    override fun FloatingIcon() {
        fun onAdd() {
            val campaign = runBlocking { createCampaign() }
            model.addCampaign(campaign)
            navigation.onCampaignAdded(campaign.uuid)
        }

        FloatingActionButton(onClick = { onAdd() }) {
            Icon(Icons.Filled.Add, contentDescription = "Add campaign")
        }
    }
}


@Composable
fun CampaignItem(campaign: Campaign, navigation: MouldNavigation, onOpen: (Campaign) -> Unit) {
    ListItem(
        headlineContent = {
            Text(campaign.name, maxLines = 1)
        },
        trailingContent = {
            IconButton(onClick = { navigation.onEditCampaign(campaign.uuid) }) {
                Icon(Icons.Filled.Edit, contentDescription = "Edit campaign")
            }
        },
        modifier = Modifier.clickable { onOpen(campaign) },
    )
}
