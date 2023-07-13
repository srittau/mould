package org.rittau.mould.model

import java.util.UUID

val BONDABLE_TYPES = setOf(WorldNoteType.PC, WorldNoteType.NPC, WorldNoteType.Faction, WorldNoteType.Location, WorldNoteType.Item)

fun canForgeBond(character: Character, noteUUID: UUID, noteType: WorldNoteType): Boolean {
    if (character.isBondedTo(noteUUID)) {
        return false
    }
    return noteType in BONDABLE_TYPES
}

class BondsTrack(ticks: Int = 0) : Track("Bonds", ticks) {
    fun forgeBond(): Int {
        return markTick()
    }

    fun clearBond(): Int {
        if (ticks > 0) ticks--
        return ticks
    }
}
