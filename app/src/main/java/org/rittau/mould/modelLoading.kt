package org.rittau.mould

import kotlinx.coroutines.runBlocking
import org.rittau.mould.model.MouldModel
import java.util.UUID

fun loadCampaigns(model: MouldModel) {
    model.setCampaigns(runBlocking { loadAllCampaigns() })
}

fun loadCharacter(model: MouldModel, campaignUUID: UUID) {
    val character = runBlocking { loadCharacter(campaignUUID) }
    val progress = runBlocking { loadProgress(campaignUUID) }
    val notes = runBlocking { loadWorldNotes() }.toMutableList()
    val journal = runBlocking { loadCampaignNotes(campaignUUID) }.toMutableList()
    model.setCharacter(character, progress, notes, journal)
}
