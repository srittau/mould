package org.rittau.mould.model

import androidx.compose.runtime.mutableStateListOf
import java.io.Serializable
import java.util.UUID

class MouldModel : Serializable {
    val campaigns = mutableStateListOf<Campaign>()

    fun findCampaign(uuid: UUID): Campaign? {
        return campaigns.find { it.uuid == uuid }
    }

    fun setCampaigns(campaigns: List<Campaign>) {
        with (this.campaigns) {
            clear()
            addAll(campaigns)
        }
    }

    fun addCampaign(campaign: Campaign) {
        campaigns.add(campaign)
    }

    fun removeCampaign(uuid: UUID) {
        campaigns.removeIf { it.uuid == uuid }
    }

    private var _character: Character? = null
    val progressTracks = mutableStateListOf<ProgressTrack>()
    val worldNotes = mutableStateListOf<WorldNote>()
    val journal = mutableStateListOf<JournalEntry>()

    val character: Character
        get() {
            val char = _character
            if (char === null) {
                throw IllegalStateException("Character not set")
            }
            return char
        }

    fun setCharacter(
        char: Character,
        tracks: List<ProgressTrack>,
        notes: List<WorldNote>,
        journal: List<JournalEntry>
    ) {
        _character = char
        with(progressTracks) {
            clear()
            addAll(tracks)
        }
        with(worldNotes) {
            clear()
            addAll(notes)
            sortBy { it.title }
        }
        with(this.journal) {
            clear()
            addAll(journal)
        }
    }

    fun findProgressTrack(uuid: UUID): ProgressTrack? {
        return progressTracks.find { it.uuid == uuid }
    }

    fun addProgressTrack(track: ProgressTrack) {
        progressTracks.add(track)
    }

    fun removeProgressTrack(uuid: UUID) {
        progressTracks.removeIf { it.uuid == uuid }
    }

    fun findWorldNote(uuid: UUID): WorldNote? {
        return worldNotes.find { it.uuid == uuid }
    }

    fun addWorldNote(note: WorldNote) {
        with(worldNotes) {
            worldNotes.add(note)
            sortBy { it.title }
        }
    }

    fun updateWorldNote(note: WorldNote) {
        with(worldNotes) {
            removeIf { it.uuid == note.uuid }
            add(note)
            sortBy { it.title }
        }
    }

    fun removeWorldNote(uuid: UUID) {
        worldNotes.removeIf { it.uuid == uuid }
    }

    fun findJournalEntry(uuid: UUID): JournalEntry? {
        return journal.find { it.uuid == uuid }
    }

    fun addJournalEntry(entry: JournalEntry) {
        journal.add(0, entry)
    }

    fun removeJournalEntry(uuid: UUID) {
        journal.removeIf { it.uuid == uuid }
    }
}
