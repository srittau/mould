package org.rittau.mould.model

import androidx.compose.runtime.mutableStateListOf
import java.io.Serializable
import java.util.UUID

class MouldModel : Serializable {
    private var _character: Character? = null
    val progressTracks = mutableStateListOf<ProgressTrack>()
    val worldNotes = mutableStateListOf<WorldNote>()
    val journal = mutableStateListOf<CampaignNote>()

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
        journal: List<CampaignNote>
    ) {
        _character = char
        with(progressTracks) {
            clear()
            addAll(tracks)
        }
        with(worldNotes) {
            clear()
            addAll(notes)
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
        worldNotes.add(note)
    }

    fun removeWorldNote(uuid: UUID) {
        worldNotes.removeIf { it.uuid == uuid }
    }

    fun findJournalEntry(uuid: UUID): CampaignNote? {
        return journal.find { it.uuid == uuid }
    }

    fun addJournalEntry(entry: CampaignNote) {
        journal.add(0, entry)
    }

    fun removeJournalEntry(uuid: UUID) {
        journal.removeIf { it.uuid == uuid }
    }
}
