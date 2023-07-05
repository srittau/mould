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
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RenameColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import androidx.room.Upsert
import androidx.room.migration.AutoMigrationSpec
import org.rittau.mould.model.CampaignNote
import org.rittau.mould.model.Character
import org.rittau.mould.model.WorldNote
import java.util.UUID

private val CAMPAIGN_UUID = UUID.fromString("6506c0fa-d589-4b51-b454-13d1ec7002b4")
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
    @ColumnInfo val summary: String,
    @ColumnInfo val text: String,
)

@Dao
private interface WorldNoteDao {
    @Query("SELECT * FROM world_notes WHERE world_id = :worldID")
    suspend fun selectNotesByWorld(worldID: String): List<DbWorldNote>

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

@Dao
private interface CampaignDao {
    @Upsert
    suspend fun upsertCampaign(campaign: DbCampaign)
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
    @ColumnInfo val text: String,
    @ColumnInfo(defaultValue = "0") val order: Int,
)

@Dao
private interface CampaignNoteDao {
    @Query("SELECT * FROM campaign_notes WHERE campaign_uuid = :campaignUUID ORDER BY `order` DESC")
    suspend fun selectNotesByCampaign(campaignUUID: UUID): List<DbCampaignNote>

    @Insert
    suspend fun insertNote(note: DbCampaignNote)

    @Query("UPDATE campaign_notes SET title = :title, text = :text WHERE uuid = :uuid")
    suspend fun updateNote(uuid: UUID, title: String, text: String)

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
    @ColumnInfo(defaultValue = "0") val bonds: Int = 0,
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
)

@Dao
private interface CharacterDao {
    @Query("SELECT * FROM characters WHERE campaign_uuid = :campaignUUID")
    suspend fun selectCharacter(campaignUUID: UUID): List<DbCharacter>

    @Upsert
    suspend fun upsertCharacter(character: DbCharacter)
}

@Database(
    entities = [DbScenario::class, DbWorld::class, DbWorldNote::class, DbCampaign::class, DbCampaignNote::class, DbCharacter::class],
    version = 5,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
        AutoMigration(from = 4, to = 5, spec = MouldDatabase.DeleteMomentumMigration::class),
    ],
)
private abstract class MouldDatabase : RoomDatabase() {
    @DeleteColumn("characters", "maxMomentum")
    @DeleteColumn("characters", "momentumReset")
    @RenameColumn("characters", "spentExperience", "spent_experience")
    class DeleteMomentumMigration : AutoMigrationSpec

    abstract fun scenarioDao(): ScenarioDao
    abstract fun worldDao(): WorldDao
    abstract fun worldNoteDao(): WorldNoteDao
    abstract fun campaignDao(): CampaignDao
    abstract fun campaignNoteDao(): CampaignNoteDao
    abstract fun characterDao(): CharacterDao
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
        campaignDao().upsertCampaign(
            DbCampaign(
                CAMPAIGN_UUID, "default", WORLD_ID, "Default Campaign"
            )
        )
    }
}

suspend fun saveCharacter(character: Character) {
    getDb().characterDao().upsertCharacter(
        DbCharacter(
            CAMPAIGN_UUID,
            name = character.name,
            summary = character.summary,
            edge = character.edge,
            heart = character.heart,
            iron = character.iron,
            shadow = character.shadow,
            wits = character.wits,
        )
    )
}

suspend fun loadCharacter(): Character {
    val characters = getDb().characterDao().selectCharacter(CAMPAIGN_UUID)
    if (characters.isEmpty()) {
        return Character("")
    }
    val character = characters[0]
    return Character(
        character.name,
        character.summary,
        character.edge,
        character.heart,
        character.iron,
        character.shadow,
        character.wits,
    )
}


suspend fun loadWorldNotes(): List<WorldNote> {
    val notes = getDb().worldNoteDao().selectNotesByWorld(WORLD_ID)
    return notes.map { WorldNote(it.uuid, it.title, it.summary, it.text) }
}

suspend fun createWorldNote(title: String, summary: String, text: String): WorldNote {
    val note = DbWorldNote(UUID.randomUUID(), WORLD_ID, title, summary, text)
    getDb().worldNoteDao().insertNote(note)
    return WorldNote(note.uuid, note.title, note.summary, note.text)
}

suspend fun updateWorldNote(note: WorldNote) {
    getDb().worldNoteDao().updateNote(
        DbWorldNote(note.uuid, WORLD_ID, note.title, note.summary, note.text)
    )
}

suspend fun deleteWorldNote(note: WorldNote) {
    getDb().worldNoteDao().deleteNote(note.uuid)
}

suspend fun loadCampaignNotes(): List<CampaignNote> {
    val notes = getDb().campaignNoteDao().selectNotesByCampaign(CAMPAIGN_UUID)
    return notes.map { CampaignNote(it.uuid, it.title, it.text) }
}

suspend fun createCampaignNote(title: String, text: String): CampaignNote {
    val db = getDb()
    val order = (db.campaignNoteDao().selectMaxOrder(CAMPAIGN_UUID) ?: 0) + 1
    val note = DbCampaignNote(UUID.randomUUID(), CAMPAIGN_UUID, title, text, order)
    db.campaignNoteDao().insertNote(note)
    return CampaignNote(note.uuid, note.title, note.text)
}

suspend fun updateCampaignNote(note: CampaignNote) {
    getDb().campaignNoteDao().updateNote(note.uuid, note.title, note.text)
}

suspend fun deleteCampaignNote(note: CampaignNote) {
    getDb().campaignNoteDao().deleteNote(note.uuid)
}
