package org.rittau.mould

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import androidx.room.Upsert
import org.rittau.mould.model.CampaignNote
import org.rittau.mould.model.Character
import java.util.UUID

private val CAMPAIGN_UUID = UUID.fromString("6506c0fa-d589-4b51-b454-13d1ec7002b4")

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
)

@Dao
private interface CampaignNoteDao {
    @Query("SELECT * FROM campaign_notes WHERE campaign_uuid = :campaignUUID")
    suspend fun selectNotesByCampaign(campaignUUID: UUID): List<DbCampaignNote>

    @Insert
    suspend fun insertNote(note: DbCampaignNote)

    @Update
    suspend fun updateNote(note: DbCampaignNote)

    @Query("DELETE FROM campaign_notes WHERE uuid = :uuid")
    suspend fun deleteNote(uuid: UUID)
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
)

@Dao
private interface CharacterDao {
    @Query("SELECT * FROM characters WHERE campaign_uuid = :campaignUUID")
    suspend fun selectCharacter(campaignUUID: UUID): List<DbCharacter>

    @Upsert
    suspend fun upsertCharacter(character: DbCharacter)
}

@Database(
    entities = [DbScenario::class, DbWorld::class, DbCampaign::class, DbCampaignNote::class, DbCharacter::class],
    version = 1
)
private abstract class MouldDatabase : RoomDatabase() {
    abstract fun scenarioDao(): ScenarioDao
    abstract fun worldDao(): WorldDao
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
        if (!worldDao().hasWorld("default")) {
            worldDao().insertWorld(DbWorld("default", "Default World"))
        }
        campaignDao().upsertCampaign(
            DbCampaign(
                CAMPAIGN_UUID, "default", "default", "Default Campaign"
            )
        )
    }
}

suspend fun saveCharacter(character: Character) {
    getDb().characterDao().upsertCharacter(DbCharacter(CAMPAIGN_UUID, character.name))
}

suspend fun loadCharacter(): Character {
    val characters = getDb().characterDao().selectCharacter(CAMPAIGN_UUID)
    if (characters.isEmpty()) {
        return Character("")
    }
    val character = characters[0]
    return Character(character.name)
}

suspend fun loadNotes(): List<CampaignNote> {
    val notes = getDb().campaignNoteDao().selectNotesByCampaign(CAMPAIGN_UUID)
    return notes.map { CampaignNote(it.uuid, it.title, it.text) }
}

suspend fun createNote(title: String, text: String): CampaignNote {
    val note = DbCampaignNote(UUID.randomUUID(), CAMPAIGN_UUID, title, text)
    getDb().campaignNoteDao().insertNote(note)
    return CampaignNote(note.uuid, note.title, note.text)
}

suspend fun updateNote(note: CampaignNote) {
    getDb().campaignNoteDao().updateNote(
        DbCampaignNote(note.uuid, CAMPAIGN_UUID, note.title, note.text)
    )
}

suspend fun deleteNote(note: CampaignNote) {
    getDb().campaignNoteDao().deleteNote(note.uuid)
}
