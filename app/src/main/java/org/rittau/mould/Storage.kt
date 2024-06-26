package org.rittau.mould

import androidx.room.AutoMigration
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RenameColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import androidx.room.migration.AutoMigrationSpec
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.rittau.mould.model.Campaign
import org.rittau.mould.model.JournalEntry
import org.rittau.mould.model.ChallengeRank
import org.rittau.mould.model.Character
import org.rittau.mould.model.ProgressCompletion
import org.rittau.mould.model.ProgressTrack
import org.rittau.mould.model.ProgressType
import org.rittau.mould.model.WorldNote
import org.rittau.mould.model.WorldNoteType
import java.util.UUID

private const val WORLD_ID = "default"

@Entity(tableName = "scenarios")
private data class DbScenario(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,
)

@Dao
private interface ScenarioDao {
    @Query("SELECT COUNT(*) == 1 FROM scenarios WHERE id = :id")
    suspend fun hasScenario(id: String): Boolean

    @Insert
    suspend fun insertScenario(scenario: DbScenario)
}

@Entity(tableName = "worlds")
private data class DbWorld(
    @PrimaryKey val id: String,
    @ColumnInfo val name: String,
)

@Dao
private interface WorldDao {
    @Query("SELECT COUNT(*) == 1 FROM worlds WHERE id = :id")
    suspend fun hasWorld(id: String): Boolean

    @Insert
    suspend fun insertWorld(world: DbWorld)
}

@Entity(
    tableName = "world_notes",
    foreignKeys = [
        ForeignKey(
            entity = DbWorld::class,
            parentColumns = ["id"],
            childColumns = ["world_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index("world_id"),
    ]
)
private data class DbWorldNote(
    @PrimaryKey val uuid: UUID,
    @ColumnInfo(name = "world_id") val worldID: String,
    @ColumnInfo val title: String,
    @ColumnInfo(defaultValue = "Other") val type: WorldNoteType = WorldNoteType.Other,
    @ColumnInfo(defaultValue = "") val summary: String = "",
    @ColumnInfo(defaultValue = "") val text: String = "",
)


private fun worldNoteToDb(note: WorldNote): DbWorldNote {
    return DbWorldNote(
        note.uuid,
        WORLD_ID,
        note.title,
        note.type,
        note.summary,
        note.text,
    )
}

private fun worldNoteFromDb(dbNote: DbWorldNote): WorldNote {
    return WorldNote(
        dbNote.uuid,
        dbNote.title,
        dbNote.type,
        dbNote.summary,
        dbNote.text,
    )
}

@Dao
private interface WorldNoteDao {
    @Query("SELECT * FROM world_notes WHERE world_id = :worldID")
    suspend fun selectNotesByWorld(worldID: String): List<DbWorldNote>

    @Query("SELECT world_notes.* FROM world_notes JOIN bonds ON world_notes.uuid = bonds.world_note_uuid WHERE bonds.campaign_uuid = :campaignUUID")
    suspend fun selectNotesByBond(campaignUUID: UUID): List<DbWorldNote>

    @Insert
    suspend fun insertNote(note: DbWorldNote)

    @Update
    suspend fun updateNote(note: DbWorldNote)

    @Query("DELETE FROM world_notes WHERE uuid = :uuid")
    suspend fun deleteNote(uuid: UUID)
}

@Entity(
    tableName = "campaigns",
    foreignKeys = [
        ForeignKey(
            entity = DbScenario::class,
            parentColumns = ["id"],
            childColumns = ["scenario_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.RESTRICT,
        ),
        ForeignKey(
            entity = DbWorld::class,
            parentColumns = ["id"],
            childColumns = ["world_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.RESTRICT,
        ),
    ],
    indices = [
        Index("scenario_id"),
        Index("world_id"),
    ]
)
private data class DbCampaign(
    @PrimaryKey val uuid: UUID,
    @ColumnInfo(name = "scenario_id") val scenarioID: String,
    @ColumnInfo(name = "world_id") val worldID: String,
    @ColumnInfo val name: String,
)

private fun campaignToDb(campaign: Campaign): DbCampaign {
    return DbCampaign(
        campaign.uuid,
        "default",
        WORLD_ID,
        campaign.name,
    )
}

private fun campaignFromDb(db: DbCampaign): Campaign {
    return Campaign(
        db.uuid,
        db.name,
    )
}

@Dao
private interface CampaignDao {
    @Query("SELECT * FROM campaigns")
    suspend fun selectAllCampaigns(): List<DbCampaign>

    @Insert
    suspend fun insertCampaign(campaign: DbCampaign)

    @Update
    suspend fun updateCampaign(campaign: DbCampaign)

    @Query("DELETE FROM campaigns WHERE uuid = :uuid")
    suspend fun deleteCampaign(uuid: UUID)
}

@Entity(
    tableName = "campaign_notes",
    foreignKeys = [
        ForeignKey(
            entity = DbCampaign::class,
            parentColumns = ["uuid"],
            childColumns = ["campaign_uuid"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index("campaign_uuid"),
    ]
)
private data class DbCampaignNote(
    @PrimaryKey val uuid: UUID,
    @ColumnInfo(name = "campaign_uuid") val campaignUUID: UUID,
    @ColumnInfo val title: String,
    @ColumnInfo(defaultValue = "") val date: String = "",
    @ColumnInfo val text: String,
    @ColumnInfo(defaultValue = "0") val order: Int,
)

private fun campaignNoteFromDb(db: DbCampaignNote): JournalEntry {
    return JournalEntry(db.uuid, db.title, db.date, db.text)
}

@Dao
private interface CampaignNoteDao {
    @Query("SELECT * FROM campaign_notes WHERE campaign_uuid = :campaignUUID ORDER BY `order` DESC")
    suspend fun selectNotesByCampaign(campaignUUID: UUID): List<DbCampaignNote>

    @Insert
    suspend fun insertNote(note: DbCampaignNote)

    @Query("UPDATE campaign_notes SET title = :title, date = :date, text = :text WHERE uuid = :uuid")
    suspend fun updateNote(uuid: UUID, title: String, date: String, text: String)

    @Query("DELETE FROM campaign_notes WHERE uuid = :uuid")
    suspend fun deleteNote(uuid: UUID)

    @Query("SELECT MAX(`order`) FROM campaign_notes WHERE campaign_uuid = :campaignUUID")
    suspend fun selectMaxOrder(campaignUUID: UUID): Int?
}

@Entity(
    tableName = "characters", foreignKeys = [ForeignKey(
        entity = DbCampaign::class,
        parentColumns = ["uuid"],
        childColumns = ["campaign_uuid"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE,
    )]
)
private data class DbCharacter(
    @PrimaryKey @ColumnInfo(name = "campaign_uuid") val campaignUUID: UUID,
    @ColumnInfo val name: String,
    @ColumnInfo(defaultValue = "") val summary: String = "",
    @ColumnInfo(defaultValue = "0") val experience: Int = 0,
    @ColumnInfo(name = "spent_experience", defaultValue = "0") val spentExperience: Int = 0,
    @ColumnInfo(defaultValue = "1") val edge: Int = 1,
    @ColumnInfo(defaultValue = "1") val heart: Int = 1,
    @ColumnInfo(defaultValue = "1") val iron: Int = 1,
    @ColumnInfo(defaultValue = "1") val shadow: Int = 1,
    @ColumnInfo(defaultValue = "1") val wits: Int = 1,
    @ColumnInfo(defaultValue = "2") val momentum: Int = 2,
    @ColumnInfo(defaultValue = "5") val health: Int = 5,
    @ColumnInfo(defaultValue = "5") val spirit: Int = 5,
    @ColumnInfo(defaultValue = "5") val supply: Int = 5,
    @ColumnInfo(defaultValue = "FALSE") val wounded: Boolean = false,
    @ColumnInfo(defaultValue = "FALSE") val unprepared: Boolean = false,
    @ColumnInfo(defaultValue = "FALSE") val shaken: Boolean = false,
    @ColumnInfo(defaultValue = "FALSE") val encumbered: Boolean = false,
    @ColumnInfo(defaultValue = "FALSE") val maimed: Boolean = false,
    @ColumnInfo(defaultValue = "FALSE") val corrupted: Boolean = false,
    @ColumnInfo(defaultValue = "FALSE") val cursed: Boolean = false,
    @ColumnInfo(defaultValue = "FALSE") val tormented: Boolean = false,
    @ColumnInfo(defaultValue = "") val notes: String = "",
)

@Entity(
    tableName = "bonds",
    primaryKeys = ["campaign_uuid", "world_note_uuid"],
    foreignKeys = [ForeignKey(
        entity = DbCharacter::class,
        parentColumns = ["campaign_uuid"],
        childColumns = ["campaign_uuid"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE,
    ), ForeignKey(
        entity = DbWorldNote::class,
        parentColumns = ["uuid"],
        childColumns = ["world_note_uuid"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [Index("campaign_uuid"), Index("world_note_uuid")],
)
private data class DbBond(
    @ColumnInfo(name = "campaign_uuid") val characterUUID: UUID,
    @ColumnInfo(name = "world_note_uuid") val worldNoteUUID: UUID,
)

@Dao
private interface BondDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBond(bond: DbBond)

    @Query("DELETE FROM bonds WHERE campaign_uuid = :characterUUID AND world_note_uuid = :worldNoteUUID")
    suspend fun deleteBond(characterUUID: UUID, worldNoteUUID: UUID)
}

@Dao
private interface CharacterDao {
    @Transaction
    @Query("SELECT * FROM characters WHERE campaign_uuid = :campaignUUID")
    suspend fun selectCharacter(campaignUUID: UUID): List<DbCharacter>

    @Upsert
    suspend fun upsertCharacter(character: DbCharacter)
}

private fun characterToDb(character: Character): DbCharacter {
    return DbCharacter(
        campaignUUID = character.uuid,
        name = character.name,
        summary = character.summary,
        experience = character.experience,
        spentExperience = character.spentExperience,
        edge = character.edge,
        heart = character.heart,
        iron = character.iron,
        shadow = character.shadow,
        wits = character.wits,
        momentum = character.momentum,
        health = character.health,
        spirit = character.spirit,
        supply = character.supply,
        wounded = character.wounded,
        unprepared = character.unprepared,
        shaken = character.shaken,
        encumbered = character.encumbered,
        maimed = character.maimed,
        corrupted = character.corrupted,
        cursed = character.cursed,
        tormented = character.tormented,
        notes = character.notes,
    )
}

private fun characterFromDb(dbCharacter: DbCharacter, bonds: List<DbWorldNote>): Character {
    return Character(
        uuid = dbCharacter.campaignUUID,
        name = dbCharacter.name,
        summary = dbCharacter.summary,
        experience = dbCharacter.experience,
        spentExperience = dbCharacter.spentExperience,
        edge = dbCharacter.edge,
        heart = dbCharacter.heart,
        iron = dbCharacter.iron,
        shadow = dbCharacter.shadow,
        wits = dbCharacter.wits,
        momentum = dbCharacter.momentum,
        health = dbCharacter.health,
        spirit = dbCharacter.spirit,
        supply = dbCharacter.supply,
        wounded = dbCharacter.wounded,
        unprepared = dbCharacter.unprepared,
        shaken = dbCharacter.shaken,
        encumbered = dbCharacter.encumbered,
        maimed = dbCharacter.maimed,
        corrupted = dbCharacter.corrupted,
        cursed = dbCharacter.cursed,
        tormented = dbCharacter.tormented,
        notes = dbCharacter.notes,
        bonds = (bonds.map { it.uuid }).toSet(),
    )
}

@Entity(
    tableName = "progress",
    foreignKeys = [ForeignKey(
        entity = DbCampaign::class,
        parentColumns = ["uuid"],
        childColumns = ["campaign_uuid"],
        onUpdate = ForeignKey.CASCADE,
        onDelete = ForeignKey.CASCADE,
    )],
    indices = [Index("campaign_uuid")],
)
private data class DbProgress(
    @PrimaryKey @ColumnInfo val uuid: UUID,
    @ColumnInfo(name = "campaign_uuid") val campaignUUID: UUID,
    @ColumnInfo val name: String,
    @ColumnInfo(name = "challenge_rank") val challengeRank: ChallengeRank,
    @ColumnInfo(defaultValue = "Other") val type: ProgressType = ProgressType.Other,
    @ColumnInfo(defaultValue = "") val notes: String = "",
    @ColumnInfo val progress: Int = 0,
    @ColumnInfo val completion: ProgressCompletion = ProgressCompletion.InProgress,
)

private fun progressToDb(progress: ProgressTrack): DbProgress {
    return DbProgress(
        uuid = progress.uuid,
        campaignUUID = progress.campaignUUID,
        name = progress.name,
        challengeRank = progress.challengeRank,
        type = progress.type,
        notes = progress.notes,
        progress = progress.ticks,
        completion = progress.completion,
    )
}

private fun progressFromDb(dbProgress: DbProgress): ProgressTrack {
    return ProgressTrack(
        campaignUUID = dbProgress.campaignUUID,
        uuid = dbProgress.uuid,
        name = dbProgress.name,
        challengeRank = dbProgress.challengeRank,
        type = dbProgress.type,
        notes = dbProgress.notes,
        ticks = dbProgress.progress,
        completion = dbProgress.completion,
    )
}

@Dao
private interface ProgressDao {
    @Query("SELECT * FROM progress WHERE campaign_uuid = :campaignUUID AND completion = 'InProgress'")
    suspend fun selectRunningProgress(campaignUUID: UUID): List<DbProgress>

    @Insert
    suspend fun insertProgress(progress: DbProgress)

    @Update
    suspend fun updateProgress(progress: DbProgress)

    @Query("DELETE FROM progress WHERE uuid = :uuid")
    suspend fun deleteProgress(uuid: UUID)
}

@Database(
    entities = [
        DbScenario::class,
        DbWorld::class,
        DbWorldNote::class,
        DbCampaign::class,
        DbCampaignNote::class,
        DbCharacter::class,
        DbBond::class,
        DbProgress::class,
    ],
    version = 12,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5, spec = MouldDatabase.DeleteMomentumMigration::class),
        AutoMigration(from = 5, to = 6),
        AutoMigration(from = 6, to = 7, spec = MouldDatabase.DeleteBonds::class),
        AutoMigration(from = 7, to = 8),
        AutoMigration(from = 8, to = 9),
        AutoMigration(from = 9, to = 10),
        AutoMigration(from = 10, to = 11),
        AutoMigration(from = 11, to = 12),
    ],
)
private abstract class MouldDatabase : RoomDatabase() {
    @DeleteColumn("characters", "maxMomentum")
    @DeleteColumn("characters", "momentumReset")
    @RenameColumn("characters", "spentExperience", "spent_experience")
    class DeleteMomentumMigration : AutoMigrationSpec

    @DeleteColumn("characters", "bonds")
    class DeleteBonds : AutoMigrationSpec

    abstract fun scenarioDao(): ScenarioDao
    abstract fun worldDao(): WorldDao
    abstract fun worldNoteDao(): WorldNoteDao
    abstract fun campaignDao(): CampaignDao
    abstract fun campaignNoteDao(): CampaignNoteDao
    abstract fun characterDao(): CharacterDao
    abstract fun bondDao(): BondDao
    abstract fun progressDao(): ProgressDao
}

private var db: MouldDatabase? = null

private fun getDb(): MouldDatabase {
    return checkNotNull(db) { "Database not initialized" }
}

suspend fun initializeDatabase(applicationContext: android.content.Context) {
    db = Room.databaseBuilder(applicationContext, MouldDatabase::class.java, "mould").build()
    with(getDb()) {
        if (!scenarioDao().hasScenario("default")) {
            scenarioDao().insertScenario(DbScenario("default", "Default Scenario"))
        }
        if (!worldDao().hasWorld(WORLD_ID)) {
            worldDao().insertWorld(DbWorld(WORLD_ID, "Default World"))
        }
    }
}

suspend fun loadAllCampaigns(): List<Campaign> {
    val campaigns = getDb().campaignDao().selectAllCampaigns()
    return campaigns.map { campaignFromDb(it) }
}

suspend fun createCampaign(): Campaign {
    with(getDb()) {
        val dbCampaign = DbCampaign(UUID.randomUUID(), "default", WORLD_ID, "")
        campaignDao().insertCampaign(dbCampaign)
        return campaignFromDb(dbCampaign)
    }
}

suspend fun updateCampaign(campaign: Campaign) {
    with(getDb()) {
        campaignDao().updateCampaign(campaignToDb(campaign))
    }
}

suspend fun deleteCampaign(uuid: UUID) {
    with(getDb()) {
        campaignDao().deleteCampaign(uuid)
    }
}

suspend fun saveCharacter(character: Character) {
    getDb().characterDao().upsertCharacter(characterToDb(character))
}

fun saveCharacterSync(character: Character) {
    CoroutineScope(Dispatchers.IO).launch {
        saveCharacter(character)
    }
}

suspend fun loadCharacter(uuid: UUID): Character {
    val characters = getDb().characterDao().selectCharacter(uuid)
    if (characters.isEmpty()) {
        return Character(uuid, "")
    }
    val bonds = getDb().worldNoteDao().selectNotesByBond(uuid)
    return characterFromDb(characters[0], bonds)
}


suspend fun loadWorldNotes(): List<WorldNote> {
    val notes = getDb().worldNoteDao().selectNotesByWorld(WORLD_ID)
    return notes.map { worldNoteFromDb(it) }
}

suspend fun createWorldNote(
    title: String, type: WorldNoteType, summary: String, text: String
): WorldNote {
    val note = DbWorldNote(UUID.randomUUID(), WORLD_ID, title, type, summary, text)
    getDb().worldNoteDao().insertNote(note)
    return worldNoteFromDb(note)
}

suspend fun updateWorldNote(note: WorldNote) {
    getDb().worldNoteDao().updateNote(worldNoteToDb(note))
}

suspend fun deleteWorldNote(uuid: UUID) {
    getDb().worldNoteDao().deleteNote(uuid)
}

suspend fun loadCampaignNotes(campaignUUID: UUID): List<JournalEntry> {
    val notes = getDb().campaignNoteDao().selectNotesByCampaign(campaignUUID)
    return notes.map { campaignNoteFromDb(it) }
}

suspend fun createCampaignNote(
    campaignUUID: UUID, title: String = "", date: String = "", text: String = ""
): JournalEntry {
    val db = getDb()
    val order = (db.campaignNoteDao().selectMaxOrder(campaignUUID) ?: 0) + 1
    val note = DbCampaignNote(UUID.randomUUID(), campaignUUID, title, date, text, order)
    db.campaignNoteDao().insertNote(note)
    return campaignNoteFromDb(note)
}

suspend fun updateCampaignNote(note: JournalEntry) {
    getDb().campaignNoteDao().updateNote(note.uuid, note.title, note.date, note.text)
}

suspend fun deleteCampaignNote(note: JournalEntry) {
    getDb().campaignNoteDao().deleteNote(note.uuid)
}

suspend fun createBond(characterUUID: UUID, worldNoteUUID: UUID) {
    getDb().bondDao().insertBond(DbBond(characterUUID, worldNoteUUID))
}

suspend fun deleteBond(characterUUID: UUID, worldNoteUUID: UUID) {
    getDb().bondDao().deleteBond(characterUUID, worldNoteUUID)
}

suspend fun loadProgress(campaignUUID: UUID): List<ProgressTrack> {
    val progress = getDb().progressDao().selectRunningProgress(campaignUUID)
    return progress.map { progressFromDb(it) }
}

suspend fun createProgress(
    campaignUUID: UUID,
    name: String = "",
    challengeRank: ChallengeRank = ChallengeRank.Troublesome,
    type: ProgressType = ProgressType.Other,
    notes: String = "",
): ProgressTrack {
    val dbProgress = DbProgress(
        uuid = UUID.randomUUID(),
        campaignUUID = campaignUUID,
        name = name,
        challengeRank = challengeRank,
        type = type,
        notes = notes,
        progress = 0,
        completion = ProgressCompletion.InProgress,
    )
    getDb().progressDao().insertProgress(dbProgress)
    return progressFromDb(dbProgress)
}

suspend fun updateProgress(progress: ProgressTrack) {
    getDb().progressDao().updateProgress(progressToDb(progress))
}

suspend fun deleteProgress(uuid: UUID) {
    getDb().progressDao().deleteProgress(uuid)
}
