package org.rittau.mould.model

import java.io.Serializable
import java.util.UUID

enum class WorldNoteType {
    Other, World, PC, NPC, Faction, Location, Item, Event,
}

data class WorldNote(
    var uuid: UUID,
    var title: String = "",
    var type: WorldNoteType = WorldNoteType.Other,
    var summary: String = "",
    var text: String = "",
) : Serializable
