package org.rittau.mould

import kotlinx.coroutines.runBlocking
import org.rittau.mould.model.MouldModel
import java.util.UUID

fun loadCharacter(model: MouldModel, campaignUUID: UUID) {
    val character = runBlocking { loadCharacter(campaignUUID) }
    val progress = runBlocking { loadProgress(campaignUUID) }
    val notes = runBlocking { loadWorldNotes() }.toMutableList()
    notes.sortBy { it.title }
    val journal = runBlocking { loadCampaignNotes(campaignUUID) }.toMutableList()
    model.setCharacter(character, progress, notes, journal)
}
